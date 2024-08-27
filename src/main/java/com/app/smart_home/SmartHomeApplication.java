package com.app.smart_home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartHomeApplication {

    public static void main(String[] args) {
//        var configuration = StreamConfiguration.kafkaStreamsProps;
//        var topology = EnergyDataTopology.buildTopology();
//        var kafkaStreams = new KafkaStreams(topology, configuration);
//
//        kafkaStreams.start();
//
//        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        SpringApplication.run(SmartHomeApplication.class, args);
    }

}
