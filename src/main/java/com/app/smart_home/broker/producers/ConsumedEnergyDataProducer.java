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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.app.smart_home.broker.topology.EnergyDataTopology.COLLECTED_ENERGY;


@Component
public class ConsumedEnergyDataProducer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // private static final String COLLECTED_ENERGY = "energy-topic";  // Update with your Kafka topic
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyDataProducer.class);

    private final KafkaProducer<String, String> collectedEnergyProducer;

    public ConsumedEnergyDataProducer() {
        this.collectedEnergyProducer = new KafkaProducer<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        ));
    }

    public void consumeEnergyForDuration(String householdId, String serialId, Double energyConsumed) {
        EnergyData energyData = EnergyData.builder()
                .householdId(householdId)
                .timestamp(new Date())
                .serialId(serialId)
                .energy(energyConsumed)
                .state(EnergyData.CollectedEnergyState.CONSUMED)
                .build();
        ProducerRecord<String, String> record = new ProducerRecord<>(COLLECTED_ENERGY, energyData.getHouseholdId(), toJson(energyData));
        LOGGER.info("[CONSUMED ENERGY] Sending record: {}", record);
        send(collectedEnergyProducer, record);
    }

//    public void startConsumingEnergyForDuration(long durationSeconds, String householdId, String serialId, Double energyConsumed) {
//        Runnable task = () -> {
//
//            EnergyData energyData = EnergyData.builder()
//                    .householdId(householdId)
//                    .timestamp(new Date())
//                    .serialId(serialId)
//                    .energy(energyConsumed)
//                    .state(EnergyData.CollectedEnergyState.CONSUMED)
//                    .build();
//            ProducerRecord<String, String> record = new ProducerRecord<>(COLLECTED_ENERGY, energyData.getHouseholdId(), toJson(energyData));
//            LOGGER.info("[CONSUMED ENERGY] Sending record: {}", record);
//            send(collectedEnergyProducer, record);
//
//        };
//
//        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
//
//        // Stop producing after the specified duration
//        scheduler.schedule(() -> {
//            scheduler.shutdown();
//            LOGGER.info("[CONSUMED ENERGY] Stopped producing after {} seconds.", durationSeconds);
//        }, durationSeconds, TimeUnit.SECONDS);
//    }


    @SneakyThrows
    private static void send(KafkaProducer<String, String> energyCollectedProducer, ProducerRecord<String, String> record) {
        try {
            energyCollectedProducer.send(record).get();
            LOGGER.info("[CONSUMED ENERGY] Message sent successfully: {}", record);
        } catch (Exception e) {
            LOGGER.error("[CONSUMED ENERGY] Failed to send message: {}", record, e);
        }
    }

    @SneakyThrows
    private static String toJson(EnergyData energyData) {
        return OBJECT_MAPPER.writeValueAsString(energyData);
    }
}
