package com.app.smart_home.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "energy_data")
public class EnergyDataDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    @Column
    private double energy;

    @Column
    @Enumerated(EnumType.STRING)
    private EnergyData.CollectedEnergyState state;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

//    @PrePersist
//    protected void onCreate() {
//        LocalDateTime now = LocalDateTime.now();
//        this.timestamp = now;
//    }


}
