package com.app.smart_home.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device {

    @Id
    @Column(name = "id")
    private String serialId;

    @ManyToOne
    private Household household;

    @Column(name = "name")
    private String name;

    @Column(name = "energyConsumption", nullable = false)
    private double energyConsumption;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DeviceState state;

    public enum DeviceState {
        ON, OFF
    }
}
