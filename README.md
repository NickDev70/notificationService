# Notification Service

Сервис уведомлений на Spring Boot с поддержкой WebSocket для отправки уведомлений в реальном времени.

## 🚀 Быстрый старт

### Предварительные требования

- Docker и Docker Compose
- Postman (для тестирования API)

### Запуск приложения

1. **Клонируйте репозиторий и перейдите в директорию проекта:**
   ```bash
   cd notificationService
   ```

2. **Запустите приложение через Docker Compose:**
   ```bash
   docker-compose -f docker/docker-compose.yml up --build
   ```

3. **Дождитесь полного запуска сервисов:**
   - PostgreSQL контейнер запустится первым
   - Приложение дождется готовности базы данных
   - Flyway выполнит миграции схемы БД
   - Приложение будет доступно на `http://localhost:8080`

### Проверка работоспособности

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/api-docs
- **Health Check:** http://localhost:8080/actuator/health

## 📋 API Endpoints

### Пользователи (`/api/users`)

#### 1. Получить всех пользователей
```http
GET http://localhost:8080/api/users
```

#### 2. Получить пользователя по ID
```http
GET http://localhost:8080/api/users/{id}
```

#### 3. Создать пользователя
```http
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "fullName": "Иван Петров",
    "notificationPeriods": ""
}
```

#### 4. Обновить пользователя
```http
PUT http://localhost:8080/api/users/{id}
Content-Type: application/json

{
    "fullName": "Иван Петров (обновлено)",
    "notificationPeriods": ""
}
```

#### 5. Удалить пользователя
```http
DELETE http://localhost:8080/api/users/{id}
```

### События (`/api/events`)

#### 1. Получить все события
```http
GET http://localhost:8080/api/events
```

#### 2. Получить событие по ID
```http
GET http://localhost:8080/api/events/{id}
```

#### 3. Создать событие
```http
POST http://localhost:8080/api/events
Content-Type: application/json

{
    "message": "Новое важное событие",
    "occurredAt": "2024-01-15T10:30:00"
}
```

#### 4. Обновить событие
```http
PUT http://localhost:8080/api/events/{id}
Content-Type: application/json

{
    "message": "Обновленное событие",
    "occurredAt": "2024-01-15T11:00:00"
}
```

#### 5. Удалить событие
```http
DELETE http://localhost:8080/api/events/{id}
```

#### 6. Поиск событий по сообщению
```http
GET http://localhost:8080/api/events/search?keyword=важное
```

#### 7. Получить события в диапазоне дат
```http
GET http://localhost:8080/api/events/date-range?start=2024-01-01T00:00:00&end=2024-01-31T23:59:59
```

#### 8. Получить события после указанной даты
```http
GET http://localhost:8080/api/events/after?dateTime=2024-01-15T00:00:00
```

## 🔌 WebSocket API

### Подключение к WebSocket

**URL:** `ws://localhost:8080/ws`

**Endpoints:**
- `/app/notification.send` - отправить уведомление всем
- `/app/notification.user` - отправить уведомление конкретному пользователю
- `/topic/notifications` - подписка на все уведомления
- `/topic/user/{userId}` - подписка на уведомления пользователя

### Примеры WebSocket сообщений

#### Отправка уведомления всем пользователям
```json
{
    "destination": "/app/notification.send",
    "body": {
        "id": 1,
        "userId": null,
        "eventTitle": "Системное уведомление",
        "eventDescription": "Обновление системы",
        "message": "Система будет обновлена в 23:00",
        "status": "SCHEDULED"
    }
}
```

#### Отправка уведомления конкретному пользователю
```json
{
    "destination": "/app/notification.user",
    "body": {
        "id": 2,
        "userId": 1,
        "eventTitle": "Персональное уведомление",
        "eventDescription": "Важное сообщение",
        "message": "У вас новое сообщение",
        "status": "SCHEDULED"
    }
}
```

## 🧪 Тестирование в Postman

### Настройка Postman Collection

1. **Создайте новую коллекцию** "Notification Service API"

2. **Добавьте переменную окружения:**
   - `base_url`: `http://localhost:8080`

3. **Создайте запросы для каждого endpoint:**

#### Примеры запросов:

**Создание пользователя:**
```http
POST {{base_url}}/api/users
Content-Type: application/json

{
    "fullName": "Тестовый пользователь",
    "notificationPeriods": ""
}
```

**Создание события:**
```http
POST {{base_url}}/api/events
Content-Type: application/json

{
    "message": "Тестовое событие",
    "occurredAt": "2024-01-15T10:30:00"
}
```

**Поиск событий:**
```http
GET {{base_url}}/api/events/search?keyword=тест
```

### Тестирование WebSocket

1. **Используйте Postman WebSocket запросы:**
   - URL: `ws://localhost:8080/ws`
   - Подключитесь к WebSocket

2. **Подпишитесь на топики:**
   ```json
   {
       "command": "SUBSCRIBE",
       "destination": "/topic/notifications"
   }
   ```

3. **Отправьте уведомление:**
   ```json
   {
       "command": "SEND",
       "destination": "/app/notification.send",
       "body": "{\"message\":\"Тестовое уведомление\",\"eventTitle\":\"Тест\"}"
   }
   ```

## 🗄️ База данных

### Структура таблиц

- **users** - пользователи системы
- **events** - события для уведомлений
- **notifications** - уведомления
- **notification_periods** - периоды уведомлений пользователей

### Подключение к БД

- **Host:** localhost:5432
- **Database:** notificationdb
- **Username:** notification
- **Password:** notification123

## 🛠️ Разработка

### Запуск тестов

```bash
mvn test
```

### Сборка проекта

```bash
mvn clean package
```

### Локальный запуск (без Docker)

1. Убедитесь, что PostgreSQL запущен на localhost:5432
2. Выполните миграции Flyway
3. Запустите приложение:
   ```bash
   mvn spring-boot:run
   ```

## 📊 Мониторинг

### Actuator Endpoints

- **Health:** http://localhost:8080/actuator/health
- **Info:** http://localhost:8080/actuator/info
- **Metrics:** http://localhost:8080/actuator/metrics

### Логи

Логи приложения выводятся в консоль с временными метками и уровнем DEBUG для Spring Scheduling.

## 🐳 Docker

### Структура Docker

- **postgres:** PostgreSQL 15 Alpine
- **app:** Spring Boot приложение на OpenJDK 17

### Полезные команды

```bash
# Остановить сервисы
docker-compose -f docker/docker-compose.yml down

# Пересобрать и запустить
docker-compose -f docker/docker-compose.yml up --build

# Просмотр логов
docker-compose -f docker/docker-compose.yml logs -f app

# Очистка volumes
docker-compose -f docker/docker-compose.yml down -v
```

## 🔧 Конфигурация

### Переменные окружения

- `SPRING_DATASOURCE_URL` - URL базы данных
- `SPRING_DATASOURCE_USERNAME` - имя пользователя БД
- `SPRING_DATASOURCE_PASSWORD` - пароль БД
- `SPRING_FLYWAY_ENABLED` - включение миграций Flyway

### Порт

Приложение по умолчанию запускается на порту **8080**.
