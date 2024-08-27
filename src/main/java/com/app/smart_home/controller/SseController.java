package com.app.smart_home.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("/event")
public class SseController {

    //private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<String, CopyOnWriteArrayList<SseEmitter>> householdEmitters = new ConcurrentHashMap<>();

    @CrossOrigin
    @GetMapping("/{householdId}")
    public SseEmitter streamEvents(@PathVariable("householdId") String householdId) {
        SseEmitter emitter = new SseEmitter();
        householdEmitters.computeIfAbsent(householdId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> householdEmitters.get(householdId).remove(emitter));
        emitter.onTimeout(emitter::complete);

        return emitter;
    }

    public void sendMessage(String householdId, String message) {
        CopyOnWriteArrayList<SseEmitter> emitters = householdEmitters.get(householdId);
        if (emitters != null) {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(message, MediaType.TEXT_PLAIN);
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
        }
    }
}