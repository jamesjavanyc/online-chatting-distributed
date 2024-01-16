package com.example.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate template;

    @PostMapping("/chat/push/fanout")
    public void fanoutMessage(@RequestParam("message") String message) {
        template.convertAndSend("/fanout-channel/all", message);
    }

    @PostMapping("/chat/push/tenant")
    public void queue(@RequestParam("message") String message, @RequestParam("tenant-id") String tenantId) {
        template.convertAndSendToUser(tenantId, "/tenant-channel", message);
    }
}
