package com.jack.location.service;

import com.jack.location.dto.WarehouseDTO;
import com.jack.location.response.BaseResponse;

import java.util.List;

public interface WarehouseService {
    BaseResponse createOrUpdateWarehouse(List<WarehouseDTO> changes);
}
