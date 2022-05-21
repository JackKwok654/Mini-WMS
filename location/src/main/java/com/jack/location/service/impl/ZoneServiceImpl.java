package com.jack.location.service.impl;

import com.jack.location.constant.BaseResponseStatus;
import com.jack.location.dao.entity.WarehouseEntity;
import com.jack.location.dao.entity.ZoneEntity;
import com.jack.location.dao.repository.WarehouseRepository;
import com.jack.location.dao.repository.ZoneRepository;
import com.jack.location.dto.ZoneDTO;
import com.jack.location.response.BaseResponse;
import com.jack.location.service.ZoneService;
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
public class ZoneServiceImpl implements ZoneService {
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createOrUpdateZone(List<ZoneDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<ZoneEntity> newZoneList = new ArrayList<>();

        List<ZoneEntity> updateZoneList = new ArrayList<>();

        log.info("[createOrUpdateZone] process changes list size: {}", changes.size());

        if (CollectionUtils.isEmpty(changes)) {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Map<String, WarehouseEntity> codeAndWarehouseMap = warehouseRepository.findAllByCodeIn(
                    changes.stream().map(ZoneDTO::getWarehouseCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(WarehouseEntity::getCode, Function.identity()));

            Map<String, List<ZoneDTO>> groupByWarehouseCodeChangeMap = changes.stream()
                    .collect(Collectors.groupingBy(ZoneDTO::getWarehouseCode));

            for (var entry : groupByWarehouseCodeChangeMap.entrySet()) {
                if (!codeAndWarehouseMap.containsKey(entry.getKey())) {
                    log.warn("[createOrUpdateZone] warehouse: {} not found ", entry.getKey());
                    continue;
                }

                Map<String, ZoneEntity> codeAndZoneMap = zoneRepository.findAllByWarehouseCodeIsAndCodeIn(
                        entry.getKey(), entry.getValue().stream().map(ZoneDTO::getCode).collect(Collectors.toList())
                ).stream().collect(Collectors.toMap(ZoneEntity::getCode, Function.identity()));

                for (ZoneDTO change : entry.getValue()) {
                    if (codeAndZoneMap.containsKey(change.getCode())) {
                        ZoneEntity entity = codeAndZoneMap.get(change.getCode());
                        entity.setPriority(change.getPriority());
                        updateZoneList.add(entity);
                        continue;
                    }

                    newZoneList.add(
                            ZoneEntity.builder()
                                    .code(change.getCode())
                                    .priority(change.getPriority())
                                    .warehouse(codeAndWarehouseMap.get(change.getWarehouseCode()))
                                    .build()
                    );
                }
            }

            if (!CollectionUtils.isEmpty(updateZoneList)) {
                log.info("[createOrUpdateZone] update list size: {} to DB", updateZoneList.size());
                zoneRepository.saveAll(updateZoneList);
            }

            if (!CollectionUtils.isEmpty(newZoneList)) {
                log.info("[createOrUpdateZone] insert list size: {} to DB", newZoneList.size());
                zoneRepository.saveAll(newZoneList);
            }
        } catch (Exception e) {
            log.error("[createOrUpdateZone] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }
        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
