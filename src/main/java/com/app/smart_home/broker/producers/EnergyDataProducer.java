package com.app.smart_home.broker.producers;

import com.app.smart_home.domain.EnergyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.app.smart_home.broker.topology.EnergyDataTopology.COLLECTED_ENERGY;

public class EnergyDataProducer {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final List<String> HOUSEHOLD_IDS = List.of("h123", "HDor2", "HSal3");
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyDataProducer.class);

    public static void main(String[] args) {
        KafkaProducer<String, String> collectedEnergyProducer =
                new KafkaProducer<>(Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092",
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
                ));

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            HOUSEHOLD_IDS.forEach(householdId -> {
                double energy = generateRealisticEnergy();
                EnergyData energyData = EnergyData.builder()
                        .householdId(householdId)
                        .timestamp(new Date())
                        .serialId("DS" + RANDOM.nextInt(1000))
                        .energy(energy)
                        .build();
                ProducerRecord<String, String> record = new ProducerRecord<>(COLLECTED_ENERGY, energyData.getHouseholdId(), toJson(energyData));
                LOGGER.info("Sending record: {}", record);
                send(collectedEnergyProducer, record);
            });
        }, 0, 1, TimeUnit.SECONDS); //frequency
//        data1.stream()
//                .map(energyData -> new ProducerRecord<>(COLLECTED_ENERGY, energyData.getHouseholdId(), toJson(energyData)))
//                .forEach(record -> send(collectedEnergyProducer, record));

        EnergyData energyData = EnergyData.builder()
                .householdId("1L")
                .timestamp(new Date())
                .serialId("DS001")
                .energy(200.00)
                .build();

        send(collectedEnergyProducer, new ProducerRecord<>(COLLECTED_ENERGY, energyData.getHouseholdId(), toJson(energyData)));

    }

    private static double generateRealisticEnergy() {
        return 50 + (100 - 50) * RANDOM.nextDouble(); // Random value between 50 and 100 kW
    }


    @SneakyThrows
    private static void send(KafkaProducer<String, String> energyCollectedProducer, ProducerRecord<String, String> record) {
        try {
            energyCollectedProducer.send(record).get();
            LOGGER.info("Message sent successfully: {}", record);
        } catch (Exception e) {
            LOGGER.error("Failed to send message: {}", record, e);
        }
    }

    @SneakyThrows
    private static String toJson(EnergyData energyData) {
        return OBJECT_MAPPER.writeValueAsString(energyData);
    }
}
