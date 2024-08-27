package com.app.smart_home.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    @MessageMapping("/energy")
    @SendTo("/topic/messages")
    public String sendMessage() {
        return "OK";
    }
}
