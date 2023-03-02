# Depersonalization service

Лёгкий в использовании сервис по деперсонализации данных, основанный на перемешивании
данных и замене конфиденциальных данных на ближайшие или похожие, подходящие по форме.

## Установка

### Docker

Установите [Docker](https://www.docker.com/). 
Загрузите контейнер Postgresql с помощью команды
```docker run —name postgresDB -p 5455:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=person -d postgres```
Таким образом, база данных будет доступна на localhost:5455

Загрузите Backend и Frontend в отдельные контейнеры и запустите.

### С помощью IntelliJ IDEA

Для запуска бэкенда вам нужно скачать postgresql или использовать h2 db. Отличаются эти методы данными в файле 
`application.properties`

#### Файл `application.properties` для использования БД h2.
```
spring.datasource.url=jdbc:h2:file:/data/sampledata
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.defer-datasource-initialization=true
spring.jpa.hibernate.ddl-auto=create-drop
```

Для использования h2 добавьте в `build.gradle` в блок `dependencies` зависимость 
```runtimeOnly 'com.h2database:h2'```

#### Файл `application.properties` для использования БД postgresql.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/person
spring.datasource.username=postgres
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

```

Для использования `postgresql` добавьте в `build.gradle` в блок `dependencies` зависимость
```runtimeOnly 'org.postgresql:postgresql'```

Для запуска фронтенда перейдите на сайт скачивания [Node.js](https://nodejs.org/en/download/).
После скачивания зайдите в IntelliJ IDEA в папку frontend и в терминале напишите команды
`npm install`. Для запуска фронтенда пропишите `npm start`.

## Использование сайта.

Сайт представляет сервис для персонализации данных с возможностью редактирования данных из таблицы.
При входе на сайт вы увидите экран авторизации. В сервисе есть система ролей, всего их три:

`user` - роль с наименьшими привилегиями, есть возможность работы только с деперсонализированными данными 
и нет доступа к первоначальным

`admin` - может взаимодействовать с обеими таблицами, может создавать новых 
пользователей сервиса и менять роли уже существующих

`root` - роли с максимальными возможностями, имеет доступ к обеим таблицам - изначальной и деперсонализированной, 
может создавать новых пользователей сервиса и менять роли уже существующих, а также добавлять новые элементы в таблицу. 

Для тестирования всех трех ролей есть универсальные комбинации логин-пароль: 

`login - root`; `password - root` - для роли суперадмина

`login - admin`; `password - admin` - для роли админа

`login - user`; `password - user` - для роли пользователя

На сайте первым делом можно загрузить собственный файл, который загрузится в таблицу и будет отображаться на экране.
Кнопка `Импорт` отвечает за загрузку таблицы с сервера.
Кнопка `Экспорт` отвечает за загрузку обновленных данных на сервер.

Есть возможность сортировки таблицу по выбранному параметру, поиск по написанному слову и выгрузка таблицы из сайта, 
формат файла можно выбрать самостоятельно.
Кнопка деперсонализации загружает новую обезличенную таблицу, которая также может быть редактируема.

### Алгоритм деперсонализации
Деперсонализация персональных данных пользователей производится путём перемешивания элементов.
Помимо перемешивания используется замена конфиденциальных данных на ближайшие, либо на случайные,
подходящие по формату. Для дат выбирается число r из отрезка [-random; random], прибавляемое
к дате. Для серии и номера паспорта инкрементально старые значения заменяются на новые, начиная
с 0000 000000, заканчивая 9999 999999, а затем начинается заново.
