package com.app.smart_home.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumedEnergy {

    private String householdId;
    private double energy;
    private EnergyData lastEnergyData;
    private EnergyData.CollectedEnergyState state;

    public ConsumedEnergy process(EnergyData energyData) {
        this.householdId = energyData.getHouseholdId();
        this.lastEnergyData = energyData;

        if (energyData.getState().equals(EnergyData.CollectedEnergyState.CONSUMED)) {
            this.state = EnergyData.CollectedEnergyState.CONSUMED;
            this.energy = this.energy + energyData.getEnergy();
        }

        return this;
    }
}
