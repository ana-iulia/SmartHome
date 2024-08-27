package com.app.smart_home.service;


import com.app.smart_home.broker.producers.ConsumedEnergyDataProducer;
import com.app.smart_home.broker.producers.EnergyDataProducer;
import com.app.smart_home.domain.Device;
import com.app.smart_home.domain.Household;
import com.app.smart_home.domain.dto.DeviceDTO;
import com.app.smart_home.domain.dto.DeviceStartDTO;
import com.app.smart_home.mapper.DeviceMapper;
import com.app.smart_home.repo.DeviceRepository;
import com.app.smart_home.repo.HouseholdRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import static com.app.smart_home.broker.ContinuousEnergyDataProducer.*;


@Service
public class DeviceService implements IDeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private ConsumedEnergyDataProducer consumedEnergyDataProducer;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyDataProducer.class);


    @Transactional
    @Override
    public DeviceDTO saveDevice(DeviceDTO deviceDTO) {
        Optional<Household> household = householdRepository.findById(deviceDTO.getHouseholdId());
        Device device = deviceMapper.toDevice(deviceDTO);
        device.setHousehold(household.get());
        device.setState(Device.DeviceState.OFF);
        return deviceMapper.toDeviceDTO(deviceRepository.save(device));
    }

    @Override
    public void startDevice(DeviceStartDTO deviceStartDTO) {
        Device device = deviceRepository.findById(deviceStartDTO.getSerialId()).get();
        device.setState(Device.DeviceState.ON);
        deviceRepository.save(device);

        //average energy consumed in a second
        double energyConsumed = deviceStartDTO.getSeconds() * device.getEnergyConsumption() / 3600;

        startConsumingEnergyForDuration(device, deviceStartDTO.getSeconds(), energyConsumed);

    }

    private void startConsumingEnergyForDuration(Device device, long durationSeconds, Double energyConsumed) {
        Runnable task = () -> {

            consumedEnergyDataProducer.consumeEnergyForDuration(device.getHousehold().getId(), device.getSerialId(), energyConsumed);

        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

        // Stop producing after the specified duration
        scheduler.schedule(() -> {
            scheduler.shutdown();
            device.setState(Device.DeviceState.OFF);
            deviceRepository.save(device);
            LOGGER.info("[CONSUMED ENERGY] Stopped producing after {} seconds.", durationSeconds);
        }, durationSeconds, TimeUnit.SECONDS);
    }


}
