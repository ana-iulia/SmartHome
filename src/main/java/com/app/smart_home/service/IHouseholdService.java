package com.app.smart_home.service;


import com.app.smart_home.domain.dto.HouseholdLogin;

public interface IHouseholdService {
    String login(HouseholdLogin loginRequest) throws Exception;

    //HouseholdDTO saveHousehold(DeviceDTO deviceDTO);
}
