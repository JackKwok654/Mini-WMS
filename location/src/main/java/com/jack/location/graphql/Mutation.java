package com.jack.location.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.jack.location.dto.*;
import com.jack.location.response.BaseResponse;
import com.jack.location.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Mutation implements GraphQLMutationResolver {
    @Autowired
    private ZoneService zoneService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private LocationTypeService locationTypeService;

    @Autowired
    private LocationGroupService locationGroupService;

    public BaseResponse createOrUpdateZone(List<ZoneDTO> changes) {
        return zoneService.createOrUpdateZone(changes);
    }

    public BaseResponse createOrUpdateLocation(List<LocationDTO> changes) {
        return locationService.createOrUpdateLocation(changes);
    }

    public BaseResponse createOrUpdateWarehouse(List<WarehouseDTO> changes) {
        return warehouseService.createOrUpdateWarehouse(changes);
    }

    public BaseResponse createOrUpdateLocationType(List<LocationTypeDTO> changes) {
        return locationTypeService.createOrUpdateLocationType(changes);
    }

    public BaseResponse createOrUpdateLocationGroup(List<LocationGroupDTO> changes) {
        return locationGroupService.createOrUpdateLocationGroup(changes);
    }
}
