package com.jack.location.service;

import com.jack.location.dto.LocationDTO;
import com.jack.location.response.BaseResponse;

import java.util.List;

public interface LocationService {
    BaseResponse createOrUpdateLocation(List<LocationDTO> changes);
}
