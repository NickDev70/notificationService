# WebSocket Testing Examples

## Подключение к WebSocket

**URL:** `ws://localhost:8080/ws`

## Примеры сообщений для тестирования

### 1. Подписка на все уведомления

```json
{
    "command": "CONNECT",
    "accept-version": "1.1,1.0",
    "heart-beat": "10000,10000"
}
```

```json
{
    "command": "SUBSCRIBE",
    "destination": "/topic/notifications",
    "id": "sub-0"
}
```

### 2. Подписка на уведомления конкретного пользователя

```json
{
    "command": "SUBSCRIBE",
    "destination": "/topic/user/1",
    "id": "sub-1"
}
```

### 3. Отправка уведомления всем пользователям

```json
{
    "command": "SEND",
    "destination": "/app/notification.send",
    "content-type": "application/json",
    "body": "{\"id\":1,\"userId\":null,\"eventTitle\":\"Системное уведомление\",\"eventDescription\":\"Обновление системы\",\"message\":\"Система будет обновлена в 23:00\",\"status\":\"SCHEDULED\"}"
}
```

### 4. Отправка уведомления конкретному пользователю

```json
{
    "command": "SEND",
    "destination": "/app/notification.user",
    "content-type": "application/json",
    "body": "{\"id\":2,\"userId\":1,\"eventTitle\":\"Персональное уведомление\",\"eventDescription\":\"Важное сообщение\",\"message\":\"У вас новое сообщение\",\"status\":\"SCHEDULED\"}"
}
```

## Тестирование в браузере

Откройте файл `src/main/resources/static/websocket-test.html` в браузере для интерактивного тестирования WebSocket.

## Тестирование в Postman

1. Создайте новый WebSocket запрос
2. URL: `ws://localhost:8080/ws`
3. Отправьте сообщения выше по порядку
4. Наблюдайте за ответами в реальном времени

## Ожидаемые ответы

### При подключении:
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
    "body": "{\"id\":1,\"userId\":null,\"eventTitle\":\"Системное уведомление\",\"message\":\"Система будет обновлена в 23:00\",\"status\":\"SCHEDULED\"}"
}
```
