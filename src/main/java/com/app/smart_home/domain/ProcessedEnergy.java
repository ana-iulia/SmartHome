package com.app.smart_home.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEnergy {
    private String householdId;
    private String deviceSeries;
    private double energyProduced;
    private EnergyData lastEnergyData;
    private EnergyData.CollectedEnergyState state;

    public ProcessedEnergy process(EnergyData energyData) {

        this.householdId = energyData.getHouseholdId();
        this.lastEnergyData = energyData;
        this.deviceSeries = energyData.getSerialId();
        this.state = EnergyData.CollectedEnergyState.REMAINED;
        if (energyData.getState().equals(EnergyData.CollectedEnergyState.CONSUMED)) {
            this.energyProduced = this.energyProduced - energyData.getEnergy();
        } else {
            this.energyProduced = this.energyProduced + energyData.getEnergy();
        }

        return this;
    }
}


