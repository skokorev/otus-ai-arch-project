workspace "Помощник архитектора" "Система рекомендаций научных статей по интересам" {
!identifiers hierarchical
    model {
        user = person "User"
        mail_server = softwareSystem "Почтовый сервер"
        inference_provider = softwareSystem "Облачный провайдер нейросетей"
        arxiv = softwareSystem "Arxiv.org"
        arch_helper = softwareSystem "Помощник архитектора" {
            front = container "Веб-интерфейс для конфигурации интересов" {
                technology Nginx
                browser = component "Фронтенд" "Интерфейс конфигуратора в браузере пользователя" {
                    technology TS
                    user -> this "Конфигурация предпочтений"
                }
                component "Сервер" "Веб-сервер для отдачи статики" {
                    technology Nginx
                    user -> this "Запрос на веб-интерфейс" "HTTP:443"
                }
            }
            back = container "Бэкенд" {
                technology Java
                api = component "Конфигуратор" "Конфигуратор предпочтений пользователя" {
                    technology Java
                    arch_helper.front.browser -> this "Конфигурация предпочтений" "HTTP:443"
                }
            }
            crawler = container "Краулер по arXiv для поиска статей" {
                technology Java
                job = component "Краулер" "Краулер по arXiv, периодический опрос" {
                    technology Java
                    this -> arxiv "Получение статей" "HTTP:443"
                }
                serverless = component "Слушатель конфигурации" "Слушатель событий изменения конфигурации" {
                    technology Java
                }
            }
            recommendations = container "Харнес для создания рекомендаций" {
                technology Java
                serverless = component "Обработчик находок" "Слушатель событий найденных статей" {
                    technology Java
                }
                job = component "Рекомендатель" "Агент для раздачи рекомендаций по найденным статьям" {
                    technology Java
                    this -> inference_provider "Обращение во внешнюю нейросеть"
                    this -> arxiv "Получение статей" "HTTP:443"
                }
            }
            email_sender = container "Коммуникатор" {
                job = component "Отправка почты" "Отправитель почты с рекомендациями статей пользователям" {
                    this -> mail_server "Отправка ссылок на статьи" "SMTP:XXX"
                }
            }

            event_queue = container "Очередь доменных событий" {
                technology Kafka
                component "Очередь конфигурации" "Очередь событий изменения конфигурации" {
                    technology topic
                    arch_helper.back.api -> this "Публикация событий изменений конфигурации" "TCP:9093"
                    arch_helper.crawler.serverless -> this "Подписка на события изменения конфигурации" "TCP:9093"
                }
                component "Очередь находок" "Очередь событий найденных статей" {
                    technology topic
                    arch_helper.crawler.job -> this "Публикация найденных статей" "TCP:9093"
                    arch_helper.recommendations.serverless -> this "Подписка на найденные статьи" "TCP:9093"
                }
                component "Очередь рекомендаций" "Очередь событий выдачи рекомендаций" "TCP:9093" {
                    technology topic
                    arch_helper.recommendations.job -> this "Публикация событий рекомендаций" "TCP:9093"
                    arch_helper.email_sender.job -> this "Подписка событий рекомендаций" "TCP:9093"
                }
                crawler -> this "Публикация рекомендаций"
                email_sender -> this "Подписка на рекомендации"
            }
            file_storage = container "Хранилище статей для анализа" {
                technology S3
                crawler -> this "Сохранение файлов"
            }
            consistent_storage = container "БД предпочтений" {
                technology PostgreSQL
                component "БД конфигураций" "БД конфигураций пользователей" {
                    arch_helper.back.api -> this "Запись/Чтение конфигурации" "TCP:5432"
                }
                component "БД краулера" "БД настроек краулера" {
                    arch_helper.crawler.serverless -> this "Запись мат.представления конфигураций" "TCP:5432"
                    arch_helper.crawler.job -> this "Чтение мат.представления конфигураций/Запись рекомендаций" "TCP:5432"
                }
                // container "БД рекомендаций" "БД сервиса выдачи рекомендаций" {
                //     arch_helper.recommendations.serverless -> this "Запись найденных статей в матпредставление" "TCP:5432"
                // }
            }
        }

        mail_server -> user "Письмо со ссылками на статьи"
    }
    views {

        systemContext arch_helper {
            include *
            autoLayout
        }

        container arch_helper {
            include *
            autoLayout
        }

        component arch_helper.front "Фронт" {
            include *
            autoLayout
        }

        component arch_helper.back "Бэк" {
            include *
            autoLayout
        }

        component arch_helper.crawler "Краулер" {
            include *
            autoLayout
        }

        component arch_helper.recommendations "Харнес" {
            include *
            autoLayout
        }

        component arch_helper.email_sender "Отпрака почты" {
            include *
            autoLayout
        }
    }
}