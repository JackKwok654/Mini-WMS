package com.jack.location.service.impl;

import com.jack.location.constant.BaseResponseStatus;
import com.jack.location.dao.entity.LocationTypeEntity;
import com.jack.location.dao.repository.LocationTypeRepository;
import com.jack.location.dto.LocationTypeDTO;
import com.jack.location.response.BaseResponse;
import com.jack.location.service.LocationTypeService;
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
public class LocationTypeServiceImpl implements LocationTypeService {
    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createOrUpdateLocationType(List<LocationTypeDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<LocationTypeEntity> newLocationTypeList = new ArrayList<>();

        List<LocationTypeEntity> updateLocationTypeList = new ArrayList<>();

        log.info("[createOrUpdateLocationType] process changes list size: {}", changes.size());

        if (CollectionUtils.isEmpty(changes)) {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Map<String, LocationTypeEntity> codeAndLocationTypeMap = locationTypeRepository.findAllByCodeIn(
                    changes.stream().map(LocationTypeDTO::getCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(LocationTypeEntity::getCode, Function.identity()));

            for (LocationTypeDTO change : changes) {
                if (codeAndLocationTypeMap.containsKey(change.getCode())) {
                    LocationTypeEntity entity = codeAndLocationTypeMap.get(change.getCode());
                    entity.setFrozen(change.getFrozen());
                    entity.setLength(change.getLength());
                    entity.setWidth(change.getWidth());
                    entity.setHeight(change.getHeight());
                    updateLocationTypeList.add(entity);
                    continue;
                }

                newLocationTypeList.add(
                        LocationTypeEntity.builder()
                                .code(change.getCode())
                                .frozen(change.getFrozen())
                                .length(change.getLength())
                                .width(change.getWidth())
                                .height(change.getHeight())
                                .build()
                );
            }

            if (!CollectionUtils.isEmpty(updateLocationTypeList)) {
                log.info("[createOrUpdateLocationType] update list size: {} to DB", updateLocationTypeList.size());
                locationTypeRepository.saveAll(updateLocationTypeList);
            }

            if (!CollectionUtils.isEmpty(newLocationTypeList)) {
                log.info("[createOrUpdateLocationType] insert list size: {} to DB", newLocationTypeList.size());
                locationTypeRepository.saveAll(newLocationTypeList);
            }

        } catch (Exception e) {
            log.error("[createOrUpdateLocationType] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
