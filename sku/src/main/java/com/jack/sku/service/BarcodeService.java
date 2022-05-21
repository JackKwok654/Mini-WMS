package com.jack.sku.service;

import com.jack.sku.dto.BarcodeDTO;
import com.jack.sku.response.BaseResponse;

import java.util.List;

public interface BarcodeService {
    BaseResponse createSkuBarcode(List<BarcodeDTO> changes);

    BaseResponse deleteSkuBarcode(List<BarcodeDTO> changes);
}
