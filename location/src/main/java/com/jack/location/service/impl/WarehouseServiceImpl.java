package com.jack.location.service.impl;

import com.jack.location.constant.BaseResponseStatus;
import com.jack.location.dao.entity.WarehouseEntity;
import com.jack.location.dao.repository.WarehouseRepository;
import com.jack.location.dto.WarehouseDTO;
import com.jack.location.response.BaseResponse;
import com.jack.location.service.WarehouseService;
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
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createOrUpdateWarehouse(List<WarehouseDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<WarehouseEntity> newWarehouseList = new ArrayList<>();

        List<WarehouseEntity> updateWarehouseList = new ArrayList<>();

        log.info("[createOrUpdateWarehouse] process changes list size: {}", changes.size());

        if (CollectionUtils.isEmpty(changes)) {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Map<String, WarehouseEntity> codeAndWarehouseMap = warehouseRepository.findAllByCodeIn(
                    changes.stream().map(WarehouseDTO::getCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(WarehouseEntity::getCode, Function.identity()));

            for (WarehouseDTO change : changes) {
                if (codeAndWarehouseMap.containsKey(change.getCode())) {
                    WarehouseEntity entity = codeAndWarehouseMap.get(change.getCode());
                    entity.setDescription(change.getDescription());
                    entity.setName(change.getName());
                    updateWarehouseList.add(entity);
                    continue;
                }

                newWarehouseList.add(
                        WarehouseEntity.builder()
                                .code(change.getCode())
                                .name(change.getName())
                                .description(change.getDescription())
                                .build()
                );
            }

            if (!CollectionUtils.isEmpty(updateWarehouseList)) {
                log.info("[createOrUpdateWarehouse] update list size: {} to DB", updateWarehouseList.size());
                warehouseRepository.saveAll(updateWarehouseList);
            }

            if (!CollectionUtils.isEmpty(newWarehouseList)) {
                log.info("[createOrUpdateWarehouse] insert list size: {} to DB", newWarehouseList.size());
                warehouseRepository.saveAll(newWarehouseList);
            }
        } catch (Exception e) {
            log.error("[createOrUpdateWarehouse] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
