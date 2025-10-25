
# Task Management System

Это распределённая система для управления задачами, построенная на микросервисной архитектуре. Проект состоит из двух основных сервисов: `task-service` для управления задачами и `user-service` для управления пользователями.

-----

## Технологии

  * **Java 17**
  * **Spring Boot 3**
  * **PostgreSQL**
  * **Maven**
  * **Docker & Docker Compose**

-----

## Запуск проекта

Для запуска проекта вам понадобится установленный **Docker** и **Docker Compose**.

1.  **Склонируйте репозиторий:**

    ```bash
    git clone <https://github.com/0verridee/task-management.git>
    cd task-project
    ```

2.  **Настройте окружение:**
    В файле `docker-compose.yml` внутри секций `environment` для `task-api` и `user-api` убедитесь, что указаны правильные данные для подключения к базе данных (имя пользователя, пароль).

3.  **Запустите всю систему:**
    Выполните команду в корневой папке проекта (`task-project`):

    ```bash
    docker-compose up --build
    ```

    Эта команда соберёт Docker-образы для обоих сервисов, запустит их вместе с базой данных PostgreSQL и свяжет их в общую сеть.

-----

## Доступ к сервисам

После успешного запуска система будет доступна по следующим адресам:

  * **Task Service:** `http://localhost:8080`
  * **User Service:** `http://localhost:8081`

-----

## API Документация (Swagger)

Интерактивная документация для каждого сервиса доступна после запуска по следующим ссылкам:

  * **Task Service API:** [http://localhost:8080/swagger-ui.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)
  * **User Service API:** [http://localhost:8081/swagger-ui.html](https://www.google.com/search?q=http://localhost:8081/swagger-ui.html)
