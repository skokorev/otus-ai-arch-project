workspace "Помощник архитектора" "Система рекомендаций научных статей по интересам" {
!identifiers hierarchical
    model {
        user = person "User"
        admin = person "Admin"
        mail_server = softwareSystem "Почтовый сервер"
        inference_provider = softwareSystem "Облачный провайдер нейросетей"
        arxiv = softwareSystem "Arxiv.org"
        arch_helper = softwareSystem "Помощник архитектора" {
            configurator = container "Бэкенд" {
                technology Java
                api = component "Конфигуратор" "Конфигуратор предпочтений пользователя" {
                    technology Java
                    admin -> this "Конфигурация предпочтений" "HTTP:443"
                }
                serverless = component "Слушатель событий" "Слушатель событий изменений наборов" {
                    technology Java
                }
            }
            crawler = container "Краулер по arXiv для поиска статей" {
                technology Java
                job = component "Краулер" "Краулер по arXiv, периодический опрос" {
                    technology Java
                    this -> arxiv "Получение статей" "HTTP:443"
                }
                serverless = component "Слушатель событий" "Слушатель событий изменения конфигурации" {
                    technology Java
                }
            }
            recommendations = container "Харнес для создания рекомендаций" {
                technology Java
                serverless = component "Слушатель событий" "Слушатель событий найденных статей" {
                    technology Java
                }
                job = component "Рекомендатель" "Агент для раздачи рекомендаций по найденным статьям" {
                    technology Java
                    this -> inference_provider "Обращение во внешнюю нейросеть"
                }
                toolset = component "Тулы для рекомендателя" "Получение наборов для группы / Получение статей для набора" {
                    technology Java
                }
            }
            communicator = container "Коммуникатор" {
                serverless = component "Отправка почты" "Отправитель почты с рекомендациями статей пользователям" {
                    this -> mail_server "Отправка ссылок на статьи" "SMTP:587"
                }
            }

            event_queue = container "Брокер доменных событий" {
                technology Kafka
                component "Топик событий пользователя" "Топик изменений подписок пользователя" {
                    technology topic
                    arch_helper.configurator.api -> this "Публикация событий изменений конфигурации" "TCP:9092"
                    arch_helper.communicator.serverless -> this "Подписка на события изменений конфигурации" "TCP:9092"
                }
                component "Топик событий наборов" "Топик изменений наборов ArXiv" {
                    technology topic
                    arch_helper.crawler.job -> this "Публикация событий изменения наборов" "TCP:9092"
                    arch_helper.configurator.serverless -> this "Подписка на изменения наборов"  "TCP:9092"
                    arch_helper.communicator.serverless -> this "Подписка на изменения наборов"  "TCP:9092"
                    arch_helper.recommendations.serverless -> this "Подписка на изменения наборов"  "TCP:9092"
                }
                component "Топик событий групп" "Топик изменений групп пользователей" {
                    technology topic
                    arch_helper.configurator.api -> this "Публикация событий изменения групп пользователей" "TCP:9092"
                    arch_helper.crawler.serverless -> this "Подписка на события изменения групп пользователей" "TCP:9092"
                    arch_helper.recommendations.serverless -> this "Подписка на события изменения групп пользователей" "TCP:9092"
                }
                component "Топик найденных статей" "Топик найденных статей" {
                    technology topic
                    arch_helper.crawler.job -> this "Публикация событий новых найденных статей" "TCP:9092"
                    arch_helper.recommendations.serverless -> this "Подписка на события новых найденных статей" "TCP:9092"
                }
                component "Топик рекомендаций" "Топик сгенерированных рекомендаций для групп" {
                    technology topic
                    arch_helper.recommendations.job -> this "Публикация рекомендаций" "TCP:9092"
                    arch_helper.communicator.serverless -> this "Подписка на рекомендации" "TCP:9092"
                }
            }
            consistent_storage = container "БД помощника архитектора" {
                technology PostgreSQL
                component "БД конфигураций" "БД конфигураций пользователей" {
                    arch_helper.configurator.api -> this "Запись/Чтение конфигурации" "TCP:5432"
                    arch_helper.configurator.serverless -> this "Запись наборов" "TCP:5432"
                }
                component "БД коммуникатора" "БД мат.представлений коммуникатора" {
                    arch_helper.communicator.serverless -> this "Запись/Чтение" "TCP:5432"
                }
                component "БД краулера" "БД настроек краулера" {
                    arch_helper.crawler.serverless -> this "Запись мат.представлений" "TCP:5432"
                    arch_helper.crawler.job -> this "Чтение мат.представления конфигураций" "TCP:5432"
                }
                component "БД рекомендаций" "БД сервиса выдачи рекомендаций" {
                    arch_helper.recommendations.serverless -> this "Запись мат.представлений" "TCP:5432"
                    arch_helper.recommendations.job -> this "Чтение мат.представлений" "TCP:5432"
                    arch_helper.recommendations.toolset -> this "Чтение мат.представлений" "TCP:5432"
                }
            }
        }

        mail_server -> user "Письмо со ссылками на статьи"

        dev = deploymentEnvironment "Dev/Stage" {
            campus = deploymentNode "Yahoondex кампусная сеть" {
                server = deploymentNode "Server" {
                    compose = deploymentNode "Docker compose" {
                        containerInstance arch_helper.configurator
                        containerInstance arch_helper.communicator
                        containerInstance arch_helper.recommendations
                        containerInstance arch_helper.crawler

                        containerInstance arch_helper.event_queue
                        containerInstance arch_helper.consistent_storage
                    }
                }
            }
            deploymentNode "Yahoondex Protected" {
                vault = infrastructureNode "Корпоративный Vault" {
                    dev.campus.server.compose -> this "Чтение секретов" "TCP:8200"
                }
                obs_platform = infrastructureNode "Платформа наблюдаемости" {
                    dev.campus.server.compose -> this "Публикация событий наблюдаемости" "TCP:4317,4318"
                }
                keycloak = infrastructureNode "Identity provider" {
                    dev.campus.server.compose -> this "Аутентификация" "TCP:9090"
                }
            }
            deploymentNode "Интернет" {
                deploymentNode "Gmail" {
                    softwareSystemInstance mail_server
                    softwareSystemInstance inference_provider
                    softwareSystemInstance arxiv
                }
            }
        }
    }
    views {

        systemContext arch_helper {
            include *
            include user
            autoLayout
        }

        container arch_helper {
            include *
            include user
            autoLayout
        }

        component arch_helper.configurator "Configurator" {
            include *
            autoLayout
        }

        component arch_helper.crawler "Crawler" {
            include *
            autoLayout
        }

        component arch_helper.recommendations "Recommendations" {
            include *
            autoLayout
        }

        component arch_helper.communicator "Communicator" {
            include *
            autoLayout
        }

        deployment * dev {
            include *
            autoLayout
        }
    }
}