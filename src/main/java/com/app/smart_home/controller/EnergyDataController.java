package com.app.smart_home.controller;

import com.app.smart_home.domain.ProcessedEnergy;
import com.app.smart_home.service.EnergyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/energy-data")
public class EnergyDataController {

    @Autowired
    EnergyDataService energyDataService;

    @GetMapping(value = "/{householdId}", produces = "application/json")
    public ResponseEntity<ProcessedEnergy> getEnergyDataBuHouseholdId(@PathVariable("householdId") String householdId) {
        return ResponseEntity.ok(energyDataService.getEnergyDataByHouseholdId(householdId));
    }

}
