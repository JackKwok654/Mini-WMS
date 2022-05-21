package com.jack.sku.service.impl;

import com.jack.sku.constant.BaseResponseStatus;
import com.jack.sku.dao.entity.BarcodeEntity;
import com.jack.sku.dao.entity.SkuEntity;
import com.jack.sku.dao.projection.BarcodeProjection;
import com.jack.sku.dao.repository.BarcodeRepository;
import com.jack.sku.dao.repository.SkuRepository;
import com.jack.sku.dto.BarcodeDTO;
import com.jack.sku.response.BaseResponse;
import com.jack.sku.service.BarcodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BarcodeServiceImpl implements BarcodeService {
    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private BarcodeRepository barcodeRepository;

    @Override
    public BaseResponse createSkuBarcode(List<BarcodeDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        List<BarcodeEntity> newBarcodeList = new ArrayList<>();

        if (CollectionUtils.isEmpty(changes))
        {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            Set<String> existingBarcode = barcodeRepository.findBarcodeProjectionInBarcodes(
                    changes.stream().map(BarcodeDTO::getBarcode).collect(Collectors.toList())
            ).stream().map(BarcodeProjection::getBarcode).collect(Collectors.toSet());

            Map<String, SkuEntity> codeAndSkuMap = skuRepository.findAllByCodeIn(
                    changes.stream().map(BarcodeDTO::getSkuCode).collect(Collectors.toList())
            ).stream().collect(Collectors.toMap(SkuEntity::getCode, Function.identity()));

            for (BarcodeDTO change : changes) {
                if (existingBarcode.contains(change.getBarcode()) || !codeAndSkuMap.containsKey(change.getSkuCode())) {
                    if (existingBarcode.contains(change.getBarcode())) {
                        log.info("[createSkuBarcode] duplicate barcode {} found, continue process...", change.getBarcode());
                    } else {
                        log.info("[createSkuBarcode] sku {} did not exist, skip process barcode {}...",
                                change.getSkuCode(), change.getBarcode()
                        );
                    }
                    continue;
                }

                newBarcodeList.add(BarcodeEntity.builder()
                        .barcode(change.getBarcode())
                        .skuEntity(codeAndSkuMap.get(change.getSkuCode()))
                        .build()
                );
            }
            if (!CollectionUtils.isEmpty(newBarcodeList)) {
                log.info("[createSkuBarcode] changes size: {} newBarcodeList size: {}",
                        changes.size(), newBarcodeList.size());
                barcodeRepository.saveAll(newBarcodeList);
            }
        } catch (Exception e)
        {
            log.error("[createSkuBarcode] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }

    @Override
    public BaseResponse deleteSkuBarcode(List<BarcodeDTO> changes) {
        String responseMsg = "Success";

        BaseResponseStatus status = BaseResponseStatus.SUCCESS;

        if (CollectionUtils.isEmpty(changes))
        {
            responseMsg = "Empty request";
            return BaseResponse.builder()
                    .status(status.toString())
                    .message(responseMsg)
                    .build();
        }

        try {
            List<BarcodeEntity> pendingRemoveBarcodeEntityList = barcodeRepository.findAllByBarcodeIn(
                    changes.stream().map(BarcodeDTO::getBarcode).collect(Collectors.toList())
            );
            if (!CollectionUtils.isEmpty(pendingRemoveBarcodeEntityList)) {
                log.info("[deleteSkuBarcode] Removing barcodes: {}",
                        StringUtils.join(",",
                                pendingRemoveBarcodeEntityList.stream().map(BarcodeEntity::getBarcode)
                                        .collect(Collectors.toList()))
                );
                barcodeRepository.deleteAll(pendingRemoveBarcodeEntityList);
            }
        } catch (Exception e) {
            log.error("[deleteSkuBarcode] Exception: ", e);
            status = BaseResponseStatus.FAILURE;
            responseMsg = "Failure";
        }

        return BaseResponse.builder()
                .status(status.toString())
                .message(responseMsg)
                .build();
    }
}
