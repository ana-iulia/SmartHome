package com.app.smart_home.service;

import com.app.smart_home.broker.topology.EnergyDataTopology;
import com.app.smart_home.domain.ProcessedEnergy;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
public class EnergyDataService {

    @Autowired
    private KafkaStreams kafkaStreams;


    public ProcessedEnergy getEnergyDataByHouseholdId(String householdId) {
        return getStore().get(householdId);

    }

    private ReadOnlyKeyValueStore<String, ProcessedEnergy> getStore() {
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(
                EnergyDataTopology.ENERGY_DATA_STORE,
                QueryableStoreTypes.keyValueStore()
        ));
    }
}
