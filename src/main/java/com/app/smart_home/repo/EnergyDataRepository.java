package com.app.smart_home.repo;

import com.app.smart_home.domain.EnergyDataDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyDataRepository extends JpaRepository<EnergyDataDB, Integer> {
}
