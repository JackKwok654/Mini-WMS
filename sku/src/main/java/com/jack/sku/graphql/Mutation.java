package com.jack.sku.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.jack.sku.dto.BarcodeDTO;
import com.jack.sku.dto.SkuDTO;
import com.jack.sku.response.BaseResponse;
import com.jack.sku.service.BarcodeService;
import com.jack.sku.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Mutation implements GraphQLMutationResolver {
    @Autowired
    private SkuService skuService;

    @Autowired
    private BarcodeService barcodeService;

    public BaseResponse createOrUpdateSku(List<SkuDTO> skuList)
    {
        log.info("[createOrUpdateSku] Starting mutation...");
        BaseResponse response = skuService.createOrUpdateSku(skuList);
        log.info("[createOrUpdateSku] Finished mutation");
        return response;
    }

    public BaseResponse createSkuBarcode(List<BarcodeDTO> barcodeList)
    {
        log.info("[createSkuBarcode] Starting mutation...");
        BaseResponse response = barcodeService.createSkuBarcode(barcodeList);
        log.info("[createSkuBarcode] Finished mutation");
        return response;
    }

    public BaseResponse deleteSkuBarcode(List<BarcodeDTO> barcodeList)
    {
        log.info("[deleteSkuBarcode] Starting mutation...");
        BaseResponse response = barcodeService.deleteSkuBarcode(barcodeList);
        log.info("[deleteSkuBarcode] Finished mutation");
        return response;
    }
}
