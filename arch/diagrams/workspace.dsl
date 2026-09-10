workspace "Помощник архитектора" "Система рекомендаций научных статей по интересам" {
    model {
        user = person "User"
        mail_server = softwareSystem "Почтовый сервер"
        inference_provider = softwareSystem "Облачный провайдер нейросетей"
        arxiv = softwareSystem "Arxiv.org"
        arch_helper = softwareSystem "Помощник архитектора" {
            front = container "Веб-интерфейс для конфигурации интересов" {
                user -> this "Конфигурация предпочтений"
            }
            back = container "Бэкенд" {
                technology Java
                front -> this "Конфигурация предпочтений" "HTTP:443"
            }
            crawler = container "Краулер по arXiv для поиска статей" {
                technology Java
                
            }
            recommendations = container "Харнес для создания рекомендаций" {
                technology Java
                this -> inference_provider "Обращение во внешнюю нейросеть"
                this -> arxiv "Получение статей" "HTTP:443"
            }
            email_sender = container "Коммуникатор" {
                this -> mail_server "Отправка ссылок на статьи" "SMTP:XXX"
            }

            event_queue = container "Очередь доменных событий" {
                technology Kafka
                back -> this "Публикация событий изменений конфигурации"
                crawler -> this "Публикация рекомендаций/подписка на события изменения конфигурации"
                email_sender -> this "Подписка на рекомендации"
            }
            file_storage = container "Хранилище статей для анализа" {
                technology S3
                crawler -> this "Сохранение файлов"
            }
            consistent_storage = container "БД предпочтений" {
                technology PostgreSQL
                back -> this "Запись/Чтение конфигурации" "TCP:5432"
                crawler -> this "Чтение конфигураций" "TCP:5432"
                email_sender -> this "Чтение предпочтений пользователей" "TCP:5432"
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
    }
}