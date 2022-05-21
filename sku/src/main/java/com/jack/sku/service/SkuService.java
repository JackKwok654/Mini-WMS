package com.jack.sku.service;

import com.jack.sku.dto.SkuDTO;
import com.jack.sku.response.BaseResponse;

import java.util.List;

public interface SkuService {
    BaseResponse createOrUpdateSku(List<SkuDTO> skuList);
}
