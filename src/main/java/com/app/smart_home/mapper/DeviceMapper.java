package com.app.smart_home.mapper;


import com.app.smart_home.domain.Device;
import com.app.smart_home.domain.Household;
import com.app.smart_home.domain.dto.DeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "householdId", source = "household", qualifiedByName = "mapHouseholdToString")
    DeviceDTO toDeviceDTO(Device device);

    Device toDevice(DeviceDTO deviceDTO);

    @Named("mapHouseholdToString")
    default String mapHouseholdToString(Household household) {
        return household != null ? household.getId() : null;
    }
}
