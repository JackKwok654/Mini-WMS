package com.jack.sku.service.impl;

import com.jack.sku.constant.BaseResponseStatus;
import com.jack.sku.dao.entity.SkuEntity;
import com.jack.sku.dao.repository.SkuRepository;
import com.jack.sku.dto.SkuDTO;
import com.jack.sku.response.BaseResponse;
import com.jack.sku.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuRepository skuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createOrUpdateSku(List<SkuDTO> skuList) {
        boolean hasError = false;

        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<SkuEntity> createSkuList = new ArrayList<>();

        List<SkuEntity> updateSkuList = new ArrayList<>();

        if (CollectionUtils.isEmpty(skuList))
        {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Map<String, SkuDTO> requestSkuMap = skuList.stream()
                    .collect(Collectors.toMap(SkuDTO::getCode, Function.identity()));

            Map<String, SkuEntity> dbSkuMap = skuRepository.findAllByCodeIn(requestSkuMap.keySet()).stream()
                    .collect(Collectors.toMap(SkuEntity::getCode, Function.identity()));

            Set<String> updateSkuCodes = dbSkuMap.keySet();

            Set<String> createSkuCodes = new HashSet<>(requestSkuMap.keySet());
            createSkuCodes.removeAll(updateSkuCodes);

            log.info("[createOrUpdateSku] updateSkuSize: {}, createSkuSize: {}",
                    updateSkuCodes.size(), createSkuCodes.size());

            for (String createSkuCode : createSkuCodes) {
                try {
                    SkuEntity newSkuEntity = new SkuEntity(requestSkuMap.get(createSkuCode));
                    createSkuList.add(newSkuEntity);
                } catch (Exception e) {
                    log.error("[createOrUpdateSku] error found when create sku: {}", createSkuCode, e);
                    hasError = true;
                }
            }
            if (!CollectionUtils.isEmpty(createSkuList)) {
                skuRepository.saveAll(createSkuList);
            }

            for (String updateSkuCode : updateSkuCodes) {
                try {
                    SkuEntity dbSku = dbSkuMap.get(updateSkuCode);
                    SkuDTO requestSku = requestSkuMap.get(updateSkuCode);
                    // TODO: filter out doesn't require update sku...

                    dbSku.setName(requestSku.getName());
                    dbSku.setDescription(requestSku.getDescription());
                    dbSku.setLength(requestSku.getLength());
                    dbSku.setWidth(requestSku.getWidth());
                    dbSku.setHeight(requestSku.getHeight());
                    updateSkuList.add(dbSku);
                } catch (Exception e) {
                    log.error("[createOrUpdateSku] error found when update sku: {}", updateSkuCode, e);
                    hasError = true;
                }
            }
            if (!CollectionUtils.isEmpty(updateSkuList)) {
                skuRepository.saveAll(updateSkuList);
            }

            if (hasError) {
                responseMsg = "Error occurred!";
            }
        } catch (Exception e)
        {
            log.error("[createOrUpdateSku] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
