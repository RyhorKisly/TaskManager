# Тема проекта: Управление задачами (Task Management System)

Цель проекта - создать удобную систему для управления задачами, которая позволит пользователям эффективно организовывать
и контролировать выполнение своих задач.

___


## Особенности реализации проекта и используемые технологии

- Проект представлен в виде мультисервиса;
- Spring boot;
- Spring JPA;
- Hibernate
- PostgreSQL
- Spring MVC;
- Spring security;
- Spring mail (Для отправки почты с целью верификации пользователя);
- Swagger (К проекту прилагается open api files к каждому сервиису);
- Njinx (Для взаимодействия между сервисами используется njinx);
- Feign (Для отправки запроса между сервисами используется feign);
- Docker (Проект можно поднять в Docker);

___

## Описание функционала:

Система должна обеспечивать следующий функционал:

1. Права и доступ
2. Операции с Пользователями
3. Операции с Проектами
4. Операции с Задачами
5. Аналитика и отчетность

### Права и доступ  

- Разграничен доступ к определённым урлам для пользователей и администратора. Далее по тексту будет указано, 
    что может делать пользователь, а что администратор

### Операции с Пользователями

1. Создание пользователя:
    - Пользователя может создать администратор;
    - Пользователь может быть создан при самостоятельной регистрации;
    - Для шифрования паролей используется PasswordEncoder
    - Информация о создании пользователя отправляется в аудит
2. Просмотр пользователей:
    - Только администратор моет просматривать страницы пользователей или любого определённого пользователя;
    - Пользователь может получить информацию о себе зайдя на доступный урл для авторизованных пользователей
   - Информация о пользователе для просмотра: id, логин, ФИО, дата создания, дата обновления, роль, статус
3. Обновление Пользователей:
    - Обновлять информацию о пользователе может только администратор;
   - Что можно обновить: логин, ФИО, роль, статус
   - Информация об обновлении пользователя отправляется в аудит
4. Дополнительный функционал:
    - После регистрации пользователь должен пройти верификацию посредством подтверждения
      своего email (Пользователю на почту отправляется письмо с просьбой перейти по ссылке.
      После этого пользователь получает новый статус и может осуществить вход в систему ввдея логин и пароль);
    - Зайти в аккаунт можно только после верификации и последующей авторизации пользователя
      посредством предоставление ему токена авторизации;

### Операции с Проектами

1. Создание Проекта:
    - Проект может быть создан только администратором;
    - В Проект могут быть добавлены только зарегистрированные и верифицированные пользователи;
   - Информация о создании Проекта отправляется в аудит
2. Просмотр Проекта:
    - Администратор может просматривать как страницы с Проектами так и отдельные проекты;
    - Администратор может выбирать для просмотра как активные так и архивные проекты;
    - Пользователь может просмотреть только тот проект в котором принимает участие;
    - Информация о Проекте для просмотра: id, дата создания, дата обновления, имя Проекта, 
   описание, менеджер проекта, участники проекта, статус проекта
3. Обновление Проекта:
    - Обновлять проект может только администратор;
    - Что можно обновить: имя Проекта, описание, менеджер проекта, участники проекта, статус проекта;
    - При обновлении менеджера или участников, проверяется наличие его на текущий момент в базе пользователей
    - Информация об обновлении Проекта отправляется в аудит

### Операции с Задачами

1. Создание Задачи:
    - Задача может быть создан только администратором;
    - Исполнителем задачи может быть добавлен только зарегистрированный и верифицированный пользователь;
    - Информация о создании Задачи отправляется в аудит
2. Просмотр Задачи:
    - Администратор может просматривать как страницы с Задачами, а также отдельные задачи;
    - Авторизованные пользователи могут выбирать для просмотра задачи проектов в которых они состоят. 
    - Также для просмотра доступны задачи других пользователей, но только по проектам, 
   в которых участвует запрашиваемый пользователь;
    - Информация о Задаче для просмотра: id, дата создания, дата обновления, тема Задачи,
      описание, статус Задачи, исполнитель
3. Обновление Задачи:
    - Обновлять Задачи может только администратор
    - Что можно обновить: тема Задачи, описание, статус Задачи, исполнитель
    - При обновлении исполнителя, проверяется наличие его на текущий момент в базе пользователей
    - Информация об обновлении Задачи отправляется в аудит

### Аналитика и отчетность

1. Создание Аудита:
    - Аудит создаётся автоматически при создании или обновлении какой-либо из существующих сущностей;
    - Администратор и пользователи не имеют доступа к созданию аудита;
2. Просмотр Аудита:
    - Администратор имеет доступ к просмотру страницы аудитов или отдельному аудиту
___

## Инструкция по запуску проекта в Docker

Для запуска проекта в Docker потребуется:
- apache-maven-3.9.2;
- Docker

Перед созданием контейнеров:
- Для отправки почты и верификации необходимо ввести логин и пароль от вашей почты в файле
   - TaskManager/user-service/src/main/resources/confidential.yml
   - Для корректной работы почты, пароль необходимо ввести специальный для приложений
   - Реализована отправка почти при помощи mail.ru или google.com
- Выполните команду 'мmn install' в корневой директории соответствующего сервиса для shared-resource;
- После, выполните команду 'mvn package' в корневых директориях соответствующих сервисов  для user-service, task-service, audit-service;
- Выполните команду 'docker-compose up' для сборки проекта в docker
- Для просмотра урлов доступен open api file по адресу http://loccalhost:81
- Для доступа к базе данных доступен PJAdmin по адресу http://localhost:82 
  - Логин: admin@admin.admin
  - Пароль: admin
  - Пароль к серверам: root
- В базе изначально создан пользователь с ролью ADMIN:
  - Логин: admin@admin.admin
  - Пароль: 1234
- Для отправки запросов рекомендую использовать Postman:
  - коллекции для данного проекта можно найти в папке postman-collections корневой директории проекта
