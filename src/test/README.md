# Unit тесты для Notification Service

## Структура тестов

### Unit тесты сервисов
- `UserServiceTest` - тестирует CRUD операции для пользователей
- `EventServiceTest` - тестирует CRUD операции для событий  
- `NotificationServiceTest` - тестирует создание и отправку уведомлений
- `WebSocketServiceTest` - тестирует отправку WebSocket сообщений

### Unit тесты контроллеров
- `UserControllerTest` - тестирует REST API для пользователей
- `EventControllerTest` - тестирует REST API для событий

### Интеграционные тесты
- `UserIntegrationTest` - тестирует работу с базой данных для пользователей
- `EventIntegrationTest` - тестирует работу с базой данных для событий

### Дополнительные тесты
- `ApplicationContextTest` - проверяет загрузку контекста Spring

## Запуск тестов

### Запуск всех тестов
```powershell
mvn test
```

### Запуск конкретного теста
```powershell
mvn test -Dtest=UserServiceTest
```

### Запуск тестов по пакету
```powershell
mvn test -Dtest=com.solution.notificationService.service.*
mvn test -Dtest=com.solution.notificationService.controller.*
mvn test -Dtest=com.solution.notificationService.integration.*
```

### Запуск с подробным выводом
```powershell
mvn test -X
```

## Конфигурация

Тесты используют:
- **H2 in-memory базу данных** для интеграционных тестов
- **Mockito** для мокирования зависимостей в unit тестах
- **Spring Boot Test** для интеграционных тестов
- **JUnit 5** как основной фреймворк тестирования

## Покрытие тестами

Тесты покрывают:
- ✅ CRUD операции для пользователей и событий
- ✅ Валидацию входных данных
- ✅ Обработку ошибок
- ✅ HTTP статусы в контроллерах
- ✅ Работу с базой данных
- ✅ WebSocket функциональность
- ✅ Конвертацию между DTO и Entity

## Особенности

- Все тесты изолированы и не зависят друг от друга
- Используется транзакционный режим для интеграционных тестов
- Тесты очищают базу данных перед каждым тестом
- Мокируются внешние зависимости (WebSocket, репозитории)
