# WebSocket Testing Examples

## Подготовка к тестированию

### 1. Запуск приложения
```bash
# Запуск базы данных и приложения
docker-compose -f docker/docker-compose.yml up -d

# Проверка статуса
docker ps

# Просмотр логов
docker logs notification-app -f
```

### 2. Проверка доступности
```bash
# Проверка health endpoint
curl http://localhost:8080/actuator/health

# Проверка WebSocket endpoint
curl http://localhost:8080/ws
# Ожидаемый ответ: "Welcome to SockJS!"
```

## Подключение к WebSocket

### URL для подключения:
- **SockJS (рекомендуется):** `http://localhost:8080/ws`
- **Прямой WebSocket:** `ws://localhost:8080/ws`

## Пошаговое тестирование в Postman

### Шаг 1: Создание WebSocket запроса
1. Откройте Postman
2. Создайте новый запрос → выберите **WebSocket Request**
3. **URL:** `http://localhost:8080/ws` (НЕ `ws://`)
4. Нажмите **Connect**

### Шаг 2: Подключение (обязательно первым!)
```json
{
    "command": "CONNECT",
    "accept-version": "1.1,1.0",
    "heart-beat": "10000,10000"
}
```

**Ожидаемый ответ:**
```json
{
    "command": "CONNECTED",
    "version": "1.1",
    "heart-beat": "0,0",
    "session": "session-12345"
}
```

### Шаг 3: Подписка на уведомления

#### Подписка на все уведомления:
```json
{
    "command": "SUBSCRIBE",
    "destination": "/topic/notifications",
    "id": "sub-0"
}
```

#### Подписка на уведомления пользователя:
```json
{
    "command": "SUBSCRIBE",
    "destination": "/topic/user/1",
    "id": "sub-1"
}
```

### Шаг 4: Отправка уведомлений

#### Отправка всем пользователям:
```json
{
    "command": "SEND",
    "destination": "/app/notification.send",
    "content-type": "application/json",
    "body": "{\"id\":1,\"userId\":null,\"eventTitle\":\"Системное уведомление\",\"eventDescription\":\"Обновление системы\",\"message\":\"Система будет обновлена в 23:00\",\"status\":\"SENT\",\"sentAt\":\"2025-09-07T10:00:00\"}"
}
```

#### Отправка конкретному пользователю:
```json
{
    "command": "SEND",
    "destination": "/app/notification.user",
    "content-type": "application/json",
    "body": "{\"id\":2,\"userId\":1,\"eventTitle\":\"Персональное уведомление\",\"eventDescription\":\"Важное сообщение\",\"message\":\"У вас новое сообщение\",\"status\":\"SENT\",\"sentAt\":\"2025-09-07T10:00:00\"}"
}
```

### Шаг 5: Отключение
```json
{
    "command": "DISCONNECT",
    "receipt": "disconnect-1"
}
```

## Тестирование в браузере (рекомендуется)

### Простой способ:
1. Откройте браузер
2. Перейдите на: `http://localhost:8080/websocket-test.html`
3. Введите User ID (например, 1)
4. Нажмите **"Подключиться"**
5. Отправьте тестовое сообщение

## Полная последовательность команд

### Минимальный тест:
```json
// 1. Подключение
{"command": "CONNECT", "accept-version": "1.1,1.0", "heart-beat": "10000,10000"}

// 2. Подписка
{"command": "SUBSCRIBE", "destination": "/topic/notifications", "id": "sub-0"}

// 3. Отправка
{"command": "SEND", "destination": "/app/notification.send", "content-type": "application/json", "body": "{\"message\":\"test\"}"}

// 4. Отключение
{"command": "DISCONNECT"}
```

## Ожидаемые ответы

### При успешном подключении:
```json
{
    "command": "CONNECTED",
    "version": "1.1",
    "heart-beat": "0,0",
    "session": "session-id"
}
```

### При получении уведомления:
```json
{
    "command": "MESSAGE",
    "destination": "/topic/notifications",
    "subscription": "sub-0",
    "message-id": "msg-id",
    "content-type": "application/json",
    "body": "{\"id\":1,\"userId\":null,\"eventTitle\":\"Системное уведомление\",\"message\":\"Система будет обновлена в 23:00\",\"status\":\"SENT\"}"
}
```

## Частые ошибки и решения

### Ошибка 400 Bad Request:
- **Причина:** Неправильный URL
- **Решение:** Используйте `http://localhost:8080/ws` вместо `ws://localhost:8080/ws`

### Зависание после отправки:
- **Причина:** Неправильный JSON в body
- **Решение:** Убедитесь, что JSON в поле `body` экранирован как строка

### Не получаете сообщения:
- **Причина:** Не подписались на топик
- **Решение:** Сначала выполните SUBSCRIBE, потом SEND

### Connection refused:
- **Причина:** Приложение не запущено
- **Решение:** Запустите `docker-compose -f docker/docker-compose.yml up -d`

## Мониторинг логов

### Просмотр логов в реальном времени:
```bash
docker logs notification-app -f
```