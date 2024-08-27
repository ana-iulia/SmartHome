package com.app.smart_home.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyData {
    private String householdId;  // or apartmentId

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    public Date timestamp;

    private String serialId;
    private double energy;

    @Builder.Default
    public CollectedEnergyState state = CollectedEnergyState.CREATED;


    public enum CollectedEnergyState {
        CREATED, REMAINED, CONSUMED
    }
}
