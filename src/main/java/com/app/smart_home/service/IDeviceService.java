package com.app.smart_home.service;


import com.app.smart_home.domain.dto.DeviceDTO;
import com.app.smart_home.domain.dto.DeviceStartDTO;

public interface IDeviceService {

    DeviceDTO saveDevice(DeviceDTO deviceDTO);

    void startDevice(DeviceStartDTO deviceStartDTO);

}
