package com.jack.location.service.impl;

import com.jack.location.constant.BaseResponseStatus;
import com.jack.location.dao.entity.*;
import com.jack.location.dao.repository.*;
import com.jack.location.dto.LocationDTO;
import com.jack.location.response.BaseResponse;
import com.jack.location.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationGroupRepository locationGroupRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createOrUpdateLocation(List<LocationDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<LocationEntity> newLocationList = new ArrayList<>();

        List<LocationEntity> updateLocationList = new ArrayList<>();

        log.info("[createOrUpdateLocation] process changes list size: {}", changes.size());

        if (CollectionUtils.isEmpty(changes)) {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Map<String, LocationTypeEntity> codeAndLocationTypeMap = locationTypeRepository.findAllByCodeIn(
                    changes.stream().map(LocationDTO::getTypeCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(LocationTypeEntity::getCode, Function.identity()));

            Map<String, WarehouseEntity> codeAndWarehouseMap = warehouseRepository.findAllByCodeIn(
                    changes.stream().map(LocationDTO::getWarehouseCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(WarehouseEntity::getCode, Function.identity()));

            Map<String, LocationGroupEntity> codeAndLocationGroupMap = locationGroupRepository.findAllByCodeIn(
                    changes.stream().map(LocationDTO::getGroupCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(LocationGroupEntity::getCode, Function.identity()));

            Map<String, LocationEntity> codeAndLocationMap = locationRepository.findAllByCodeIn(
                    changes.stream().map(LocationDTO::getCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(LocationEntity::getCode, Function.identity()));

            Map<String, List<LocationDTO>> groupByWarehouseCodeChangeMap = changes.stream()
                    .collect(Collectors.groupingBy(LocationDTO::getWarehouseCode));
            for (var warehouseEntry : groupByWarehouseCodeChangeMap.entrySet()) {
                if (!codeAndWarehouseMap.containsKey(warehouseEntry.getKey())) {
                    log.warn("[createOrUpdateLocation] warehouse: {} not found ", warehouseEntry.getKey());
                    continue;
                }

                Map<String, ZoneEntity> codeAndZoneMap = zoneRepository.findAllByWarehouseCodeIsAndCodeIn(
                        warehouseEntry.getKey(), warehouseEntry.getValue().stream()
                                .map(LocationDTO::getZoneCode).collect(Collectors.toList())
                ).stream().collect(Collectors.toMap(ZoneEntity::getCode, Function.identity()));

                Map<String, List<LocationDTO>> groupByZoneCodeChangeMap = warehouseEntry.getValue()
                        .stream().collect(Collectors.groupingBy(LocationDTO::getZoneCode));
                for (var zoneEntry : groupByZoneCodeChangeMap.entrySet()) {
                    if (!codeAndZoneMap.containsKey(zoneEntry.getKey())) {
                        log.warn("[createOrUpdateLocation] warehouse: {} zone: {} not found ",
                                warehouseEntry.getKey(), zoneEntry.getKey());
                        continue;
                    }

                    Map<String, List<LocationDTO>> groupByGroupCodeChangeMap = zoneEntry.getValue()
                            .stream().collect(Collectors.groupingBy(LocationDTO::getGroupCode));
                    for (var groupEntry : groupByGroupCodeChangeMap.entrySet()) {
                        if (!codeAndLocationGroupMap.containsKey(groupEntry.getKey())) {
                            log.warn("[createOrUpdateLocation] warehouse: {} zone: {} group: {} not found ",
                                    warehouseEntry.getKey(), zoneEntry.getKey(), groupEntry.getKey());
                            continue;
                        }

                        for (LocationDTO change : groupEntry.getValue()) {
                            if (codeAndLocationMap.containsKey(change.getCode())) {
                                LocationEntity entity = codeAndLocationMap.get(change.getCode());
                                entity.setPriority(change.getPriority());
                                entity.setLocationType(codeAndLocationTypeMap.get(change.getTypeCode()));
                                entity.setLocationGroup(codeAndLocationGroupMap.get(change.getGroupCode()));
                                updateLocationList.add(entity);
                                continue;
                            }

                            newLocationList.add(
                                    LocationEntity.builder()
                                            .code(change.getCode())
                                            .priority(change.getPriority())
                                            .locationType(codeAndLocationTypeMap.get(change.getTypeCode()))
                                            .locationGroup(codeAndLocationGroupMap.get(change.getGroupCode()))
                                            .build()
                            );
                        }
                    }
                }
            }

            if (!CollectionUtils.isEmpty(updateLocationList)) {
                log.info("[createOrUpdateLocation] update list size: {} to DB", updateLocationList.size());
                locationRepository.saveAll(updateLocationList);
            }

            if (!CollectionUtils.isEmpty(newLocationList)) {
                log.info("[createOrUpdateLocation] insert list size: {} to DB", newLocationList.size());
                locationRepository.saveAll(newLocationList);
            }
        } catch (Exception e) {
            log.error("[createOrUpdateLocation] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
