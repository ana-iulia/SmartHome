package com.app.smart_home.broker.topology;

import com.app.smart_home.domain.ConsumedEnergy;
import com.app.smart_home.domain.EnergyData;
import com.app.smart_home.domain.ProcessedEnergy;
import com.app.smart_home.mapper.JsonSerde;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

@AllArgsConstructor
public class EnergyDataTopology {
    public static final String COLLECTED_ENERGY = "collected-energy";
    public static final String REMAINING_ENERGY = "remaining-energy";
    public static final String CONSUMED_ENERGY = "consumed-energy";
    public static final String CONSUMED_TOTAL_ENERGY = "consumed-total-energy";
    public static final String ENERGY_DATA_STORE = "energy-data-store";


    public static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        JsonSerde<EnergyData> collectedEnergyJsonSerde = new JsonSerde<>(EnergyData.class);
        JsonSerde<ProcessedEnergy> processedEnergyJsonSerde = new JsonSerde<>(ProcessedEnergy.class);
        JsonSerde<ConsumedEnergy> consumedEnergyJsonSerde = new JsonSerde<>(ConsumedEnergy.class);
        var energyDataKStream = streamsBuilder.stream(COLLECTED_ENERGY,
                        Consumed.with(Serdes.String(), collectedEnergyJsonSerde))
                .groupByKey()
                .aggregate(ProcessedEnergy::new,
                        (key, value, aggregate) -> aggregate.process(value),
                        Materialized.<String, ProcessedEnergy, KeyValueStore<Bytes, byte[]>>as(ENERGY_DATA_STORE)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(processedEnergyJsonSerde))

                .toStream();
        energyDataKStream.to(REMAINING_ENERGY, Produced.with(Serdes.String(), processedEnergyJsonSerde));


        energyDataKStream.mapValues((readOnlyKey, value) -> value.getLastEnergyData())
                .filter((key, value) -> value.getState().equals(EnergyData.CollectedEnergyState.CONSUMED))
                .to(CONSUMED_ENERGY, Produced.with(Serdes.String(), collectedEnergyJsonSerde));

        var energyConsumedDataKStream = streamsBuilder.stream(CONSUMED_ENERGY, Consumed.with(Serdes.String(),
                        collectedEnergyJsonSerde))
                .groupByKey()
                .aggregate(ConsumedEnergy::new,
                        (key, value, aggregate) -> aggregate.process(value),
                        Materialized.with(Serdes.String(), consumedEnergyJsonSerde))
                .toStream();
        energyConsumedDataKStream.to(CONSUMED_TOTAL_ENERGY);

        return streamsBuilder.build();
    }
}
