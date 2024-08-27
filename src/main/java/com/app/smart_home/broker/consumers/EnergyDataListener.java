package com.app.smart_home.broker.consumers;

import com.app.smart_home.controller.SseController;
import com.app.smart_home.domain.EnergyData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EnergyDataListener {

    @Autowired
    SseController sseController;
//    @Autowired
//    private EnergyWebSocketHandler webSocketHandler;

//    @KafkaListener(topics = "${spring.kafka.topic.name.collected.energy}", groupId = "${spring.kafka.consumer.group-id}")
//    public void listen(String message) {
//        System.out.printf("Received message: %s%n", message);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            EnergyData energyData = mapper.readValue(message, EnergyData.class);
//            webSocketHandler.sendMessageToUser(energyData.getHouseholdId(), energyData.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @KafkaListener(topics = "${spring.kafka.topic.name.collected.energy}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        // Handle the message
        System.out.printf("Received message: %s%n", message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            EnergyData energyData = mapper.readValue(message, EnergyData.class);
            sseController.sendMessage(energyData.getHouseholdId(), energyData.toString());
        } catch (
                JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
