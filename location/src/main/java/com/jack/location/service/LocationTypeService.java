package com.jack.location.service;

import com.jack.location.dto.LocationTypeDTO;
import com.jack.location.response.BaseResponse;

import java.util.List;

public interface LocationTypeService {
    BaseResponse createOrUpdateLocationType(List<LocationTypeDTO> changes);
}
