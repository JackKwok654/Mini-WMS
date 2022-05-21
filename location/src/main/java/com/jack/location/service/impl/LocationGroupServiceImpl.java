package com.jack.location.service.impl;

import com.jack.location.constant.BaseResponseStatus;
import com.jack.location.dao.entity.LocationGroupEntity;
import com.jack.location.dao.entity.WarehouseEntity;
import com.jack.location.dao.entity.ZoneEntity;
import com.jack.location.dao.repository.LocationGroupRepository;
import com.jack.location.dao.repository.WarehouseRepository;
import com.jack.location.dao.repository.ZoneRepository;
import com.jack.location.dto.LocationGroupDTO;
import com.jack.location.response.BaseResponse;
import com.jack.location.service.LocationGroupService;
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
public class LocationGroupServiceImpl implements LocationGroupService {
    @Autowired
    private LocationGroupRepository locationGroupRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createOrUpdateLocationGroup(List<LocationGroupDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<LocationGroupEntity> newLocationGroupList = new ArrayList<>();

        List<LocationGroupEntity> updateLocationGroupList = new ArrayList<>();

        log.info("[createOrUpdateLocationGroup] process changes list size: {}", changes.size());

        if (CollectionUtils.isEmpty(changes)) {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Map<String, WarehouseEntity> codeAndWarehouseMap = warehouseRepository.findAllByCodeIn(
                    changes.stream().map(LocationGroupDTO::getWarehouseCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(WarehouseEntity::getCode, Function.identity()));

            Map<String, LocationGroupEntity> codeAndLocationGroupMap = locationGroupRepository.findAllByCodeIn(
                    changes.stream().map(LocationGroupDTO::getCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(LocationGroupEntity::getCode, Function.identity()));

            Map<String, List<LocationGroupDTO>> groupByWarehouseCodeChangeMap = changes.stream()
                    .collect(Collectors.groupingBy(LocationGroupDTO::getWarehouseCode));

            for (var warehouseEntry : groupByWarehouseCodeChangeMap.entrySet()) {
                if (!codeAndWarehouseMap.containsKey(warehouseEntry.getKey())) {
                    log.warn("[createOrUpdateLocationGroup] warehouse: {} not found ", warehouseEntry.getKey());
                    continue;
                }

                Map<String, List<LocationGroupDTO>> groupByZoneCodeChangeMap = warehouseEntry.getValue()
                        .stream().collect(Collectors.groupingBy(LocationGroupDTO::getZoneCode));

                Map<String, ZoneEntity> codeAndZoneMap = zoneRepository.findAllByWarehouseCodeIsAndCodeIn(
                        warehouseEntry.getKey(), warehouseEntry.getValue().stream()
                                .map(LocationGroupDTO::getZoneCode).collect(Collectors.toList())
                ).stream().collect(Collectors.toMap(ZoneEntity::getCode, Function.identity()));

                for (var zoneEntry : groupByZoneCodeChangeMap.entrySet()) {
                    if (!codeAndZoneMap.containsKey(zoneEntry.getKey()))
                    {
                        log.warn("[createOrUpdateLocationGroup] warehouse: {} zone: {} not found ",
                                warehouseEntry.getKey(), zoneEntry.getKey());
                        continue;
                    }

                    for (LocationGroupDTO change : zoneEntry.getValue()) {
                        if (codeAndLocationGroupMap.containsKey(change.getCode())) {
                            LocationGroupEntity entity = codeAndLocationGroupMap.get(change.getCode());
                            entity.setZone(codeAndZoneMap.get(change.getZoneCode()));
                            entity.setPriority(change.getPriority());
                            updateLocationGroupList.add(entity);
                            continue;
                        }

                        newLocationGroupList.add(
                                LocationGroupEntity.builder()
                                        .code(change.getCode())
                                        .zone(codeAndZoneMap.get(zoneEntry.getKey()))
                                        .priority(change.getPriority())
                                        .build()
                        );
                    }
                }
            }

            if (!CollectionUtils.isEmpty(updateLocationGroupList)) {
                log.info("[createOrUpdateLocationGroup] update list size: {} to DB", updateLocationGroupList.size());
                locationGroupRepository.saveAll(updateLocationGroupList);
            }

            if (!CollectionUtils.isEmpty(newLocationGroupList)) {
                log.info("[createOrUpdateLocationGroup] insert list size: {} to DB", newLocationGroupList.size());
                locationGroupRepository.saveAll(newLocationGroupList);
            }
        } catch (Exception e) {
            log.error("[createOrUpdateLocationGroup] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
