package com.app.smart_home.service;

import com.app.smart_home.domain.Household;
import com.app.smart_home.domain.dto.HouseholdLogin;
import com.app.smart_home.repo.HouseholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseholdService implements IHouseholdService {

    @Autowired
    HouseholdRepository householdRepository;

    @Override
    public String login(HouseholdLogin loginRequest) throws Exception {
        Household household = householdRepository.findByUsername(loginRequest.getUsername());
        if (household != null) {
            return household.getId();
        }
        throw new Exception("Bad credentials");
    }


}
