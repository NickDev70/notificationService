package com.solution.notificationService;

import com.solution.notificationService.controller.EventController;
import com.solution.notificationService.controller.UserController;
import com.solution.notificationService.controller.WebSocketController;
import com.solution.notificationService.service.EventService;
import com.solution.notificationService.service.NotififcationService;
import com.solution.notificationService.service.UserService;
import com.solution.notificationService.service.WebSocketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationContextTest {

    @Autowired
    private UserController userController;

    @Autowired
    private EventController eventController;

    @Autowired
    private WebSocketController webSocketController;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private NotififcationService notificationService;

    @Autowired
    private WebSocketService webSocketService;

    @Test
    void contextLoads() {
        assertNotNull(userController);
        assertNotNull(eventController);
        assertNotNull(webSocketController);
        assertNotNull(userService);
        assertNotNull(eventService);
        assertNotNull(notificationService);
        assertNotNull(webSocketService);
    }
}
