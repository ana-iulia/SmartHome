package com.app.smart_home.broker.consumers;

import com.app.smart_home.domain.ConsumedEnergy;
import com.app.smart_home.domain.EnergyDataDB;
import com.app.smart_home.repo.EnergyDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumedEnergyDataListener {

    @Autowired
    EnergyDataRepository energyDataRepository;

    private List<EnergyDataDB> energyDataQueue = new ArrayList<>();

    @KafkaListener(topics = "${spring.kafka.topic.name.consumed.total.energy}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        System.out.printf("Received consumed message: %s%n", message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ConsumedEnergy consumedEnergy = mapper.readValue(message, ConsumedEnergy.class);
            EnergyDataDB energyData = new EnergyDataDB();
            energyData.setEnergy(consumedEnergy.getEnergy());
            energyData.setHousehold(energyData.getHousehold());
            energyData.setState(energyData.getState());
            System.out.printf("Timestamp: %s%n", energyData.getTimestamp());
            //energyDataRepository.save(energyData);
            System.out.printf("Parsed message: %s%n", consumedEnergy.getHouseholdId());
            if (energyData.getTimestamp().getHour() == 2 && energyData.getTimestamp().getMinute() == 50) {
                energyDataQueue.add(energyData);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
