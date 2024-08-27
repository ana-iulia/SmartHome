package com.app.smart_home.domain.dto;


import com.app.smart_home.domain.Device;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {

    private String serialId;

    private String householdId;

    private String name;

    private double energyConsumption;

    private String description;

    private Device.DeviceState state;
}
