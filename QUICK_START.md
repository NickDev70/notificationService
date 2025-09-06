# 🚀 Быстрый старт - Notification Service

## 1. Запуск приложения

```bash
# Клонируйте репозиторий и перейдите в директорию
cd notificationService

# Запустите через Docker Compose
docker-compose -f docker/docker-compose.yml up --build
```

## 2. Проверка работоспособности

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health

## 3. Тестирование API

### Импорт в Postman
1. Откройте Postman
2. Импортируйте файл `postman-collection.json`
3. Убедитесь, что переменная `base_url` = `http://localhost:8080`

### Основные тесты

#### Создание пользователя
```http
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "fullName": "Тестовый пользователь",
    "notificationPeriods": "MONDAY 09:00 - 18:00, TUESDAY 09:00 - 18:00"
}
```

#### Создание события
```http
POST http://localhost:8080/api/events
Content-Type: application/json

{
    "message": "Тестовое событие",
    "occurredAt": "2024-01-15T10:30:00"
}
```

#### Получение всех пользователей
```http
GET http://localhost:8080/api/users
```

#### Получение всех событий
```http
GET http://localhost:8080/api/events
```

## 4. Тестирование WebSocket

### В браузере
1. Откройте http://localhost:8080/websocket-test.html
2. Введите User ID (например, 1)
3. Нажмите "Подключиться"
4. Отправьте тестовое уведомление

### В Postman
1. Создайте новый WebSocket запрос
2. URL: `ws://localhost:8080/ws`
3. Отправьте сообщения из файла `websocket-test-examples.md`

## 5. Полезные команды

```bash
# Остановить сервисы
docker-compose -f docker/docker-compose.yml down

# Просмотр логов
docker-compose -f docker/docker-compose.yml logs -f app

# Пересборка
docker-compose -f docker/docker-compose.yml up --build

# Очистка volumes
docker-compose -f docker/docker-compose.yml down -v
```

## 6. Структура API

### Пользователи: `/api/users`
- `GET /` - получить всех пользователей
- `GET /{id}` - получить пользователя по ID
- `POST /` - создать пользователя
- `PUT /{id}` - обновить пользователя
- `DELETE /{id}` - удалить пользователя

### События: `/api/events`
- `GET /` - получить все события
- `GET /{id}` - получить событие по ID
- `POST /` - создать событие
- `PUT /{id}` - обновить событие
- `DELETE /{id}` - удалить событие
- `GET /search?keyword=...` - поиск по сообщению
- `GET /date-range?start=...&end=...` - события в диапазоне дат
- `GET /after?dateTime=...` - события после даты

### WebSocket: `/ws`
- `/app/notification.send` - отправить всем
- `/app/notification.user` - отправить пользователю
- `/topic/notifications` - подписка на все
- `/topic/user/{id}` - подписка на пользователя

## 7. Мониторинг

- **Health:** http://localhost:8080/actuator/health
- **Metrics:** http://localhost:8080/actuator/metrics
- **API Docs:** http://localhost:8080/api-docs

## 8. База данных

- **Host:** localhost:5432
- **Database:** notificationdb
- **User:** notification
- **Password:** notification123

---

**Готово!** Приложение запущено и готово к тестированию. Используйте Swagger UI для интерактивного тестирования API или импортируйте Postman коллекцию для автоматизированного тестирования.
