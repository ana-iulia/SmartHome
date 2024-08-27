package com.app.smart_home;

import com.app.smart_home.broker.topology.EnergyDataTopology;
import com.app.smart_home.domain.ConsumedEnergy;
import com.app.smart_home.domain.EnergyData;
import com.app.smart_home.domain.ProcessedEnergy;
import com.app.smart_home.mapper.JsonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.app.smart_home.domain.EnergyData.CollectedEnergyState.CONSUMED;
import static com.app.smart_home.domain.EnergyData.CollectedEnergyState.REMAINED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class SmartHomeApplicationTests {


    TopologyTestDriver testDriver;
    private TestInputTopic<String, EnergyData> collectedEnergyTopic;
    private TestOutputTopic<String, ProcessedEnergy> processedEnergyTopic;
    private TestOutputTopic<String, EnergyData> consumedEnergyTopic;
    private TestOutputTopic<String, ConsumedEnergy> consumedTotalEnergyTopic;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        testDriver = new TopologyTestDriver(EnergyDataTopology.buildTopology(), props);

        var processedEnergyJsonSerde = new JsonSerde<>(ProcessedEnergy.class);
        var collectedEnergyJsonSerde = new JsonSerde<>(EnergyData.class);
        var consumedEnergyJsonSerde = new JsonSerde<>(EnergyData.class);
        var consumedTotalEnergyJsonSerde = new JsonSerde<>(ConsumedEnergy.class);

        collectedEnergyTopic = testDriver.createInputTopic(EnergyDataTopology.COLLECTED_ENERGY,
                Serdes.String().serializer(), collectedEnergyJsonSerde.serializer());
        processedEnergyTopic = testDriver.createOutputTopic(EnergyDataTopology.REMAINING_ENERGY, Serdes.String().deserializer(), processedEnergyJsonSerde.deserializer());
        consumedEnergyTopic = testDriver.createOutputTopic(EnergyDataTopology.CONSUMED_ENERGY, Serdes.String().deserializer(), consumedEnergyJsonSerde.deserializer());
        consumedTotalEnergyTopic = testDriver.createOutputTopic(EnergyDataTopology.CONSUMED_TOTAL_ENERGY, Serdes.String().deserializer(), consumedTotalEnergyJsonSerde.deserializer());
    }

    @AfterEach
    void teardown() {
        testDriver.close();
    }

    @Test
    void testTopology() {
        List.of(
                        EnergyData.builder()
                                .householdId("1L")
                                .timestamp(new Date())
                                .serialId("DS001")
                                .energy(100.00)
                                .build(),
                        EnergyData.builder()
                                .householdId("2L")
                                .timestamp(new Date())
                                .serialId("DS002")
                                .energy(300.50)
                                .build(),
                        EnergyData.builder()
                                .householdId("1L")
                                .timestamp(new Date())
                                .serialId("DS003")
                                .energy(50.00)
                                .state(CONSUMED)
                                .build(),
                        EnergyData.builder()
                                .householdId("1L")
                                .timestamp(new Date())
                                .serialId("DS001")
                                .energy(20.00)
                                .state(CONSUMED)
                                .build()
                )
                .forEach(energyData -> collectedEnergyTopic.pipeInput(energyData.getHouseholdId(), energyData));

        var firstEnergyData = processedEnergyTopic.readValue();
        assertEquals("1L", firstEnergyData.getHouseholdId());
        assertEquals(100.00D, firstEnergyData.getEnergyProduced());
        assertEquals(REMAINED, firstEnergyData.getState());

        var secondEnergyData = processedEnergyTopic.readValue();
        assertEquals("2L", secondEnergyData.getHouseholdId());
        assertEquals(300.50D, secondEnergyData.getEnergyProduced());
        assertEquals(REMAINED, secondEnergyData.getState());

        var thirdEnergyData = processedEnergyTopic.readValue();
        assertEquals("1L", thirdEnergyData.getHouseholdId());
        assertEquals(50.00D, thirdEnergyData.getEnergyProduced());
        assertEquals(REMAINED, thirdEnergyData.getState());

        var fourthEnergyData = processedEnergyTopic.readValue();
        assertEquals("1L", fourthEnergyData.getHouseholdId());
        assertEquals(30.00D, fourthEnergyData.getEnergyProduced());
        assertEquals(REMAINED, fourthEnergyData.getState());


        var firstConsumedEnergyData = consumedTotalEnergyTopic.readValue();
        assertEquals("1L", firstConsumedEnergyData.getHouseholdId());
        assertEquals(50.00D, firstConsumedEnergyData.getEnergy());
        assertEquals(CONSUMED, firstConsumedEnergyData.getState());

        var secondConsumedEnergyData = consumedTotalEnergyTopic.readValue();
        assertEquals("1L", secondConsumedEnergyData.getHouseholdId());
        assertEquals(70.00D, secondConsumedEnergyData.getEnergy());
        assertEquals(CONSUMED, secondConsumedEnergyData.getState());


        assertTrue(consumedTotalEnergyTopic.isEmpty());
    }

    private static void injectStaticField(Class<?> clazz, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

//    @Test
//    void testTopologyWhenRejection() {
//        List.of(
//                        BankTransaction.builder()
//                                .id(1L)
//                                .balanceId(1L)
//                                .time(new Date())
//                                .amount(new BigDecimal(-500))
//                                .build(),
//                        BankTransaction.builder()
//                                .id(2L)
//                                .balanceId(2L)
//                                .time(new Date())
//                                .amount(new BigDecimal(3000)).build(),
//                        BankTransaction.builder()
//                                .id(3L)
//                                .balanceId(1L)
//                                .time(new Date())
//                                .amount(new BigDecimal(500)).build()
//                )
//                .forEach(bankTransaction -> collectedEnergyTopic.pipeInput(bankTransaction.getBalanceId(), bankTransaction));
//
//        var firstBalance = processedEnergyTopic.readValue();
//
//        assertEquals(1L, firstBalance.getId());
//        assertEquals(new BigDecimal(0), firstBalance.getAmount());
//
//        var secondBalance = processedEnergyTopic.readValue();
//
//        assertEquals(2L, secondBalance.getId());
//        assertEquals(new BigDecimal(3000), secondBalance.getAmount());
//
//
//        var thirdBalance = processedEnergyTopic.readValue();
//
//        assertEquals(1L, thirdBalance.getId());
//        assertEquals(new BigDecimal(500), thirdBalance.getAmount());
//
//        var bankTransaction = consumedEnergyTopic.readValue();
//
//        assertEquals(1L, bankTransaction.getId());
//        assertEquals(BankTransaction.BankTransactionState.REJECTED, bankTransaction.getState());
//    }

}
