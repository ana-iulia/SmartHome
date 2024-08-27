package com.app.smart_home.repo;


import com.app.smart_home.domain.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, String> {
    Household findByUsername(String username);


}
