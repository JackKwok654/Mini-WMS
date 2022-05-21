package com.jack.location.service;

import com.jack.location.dto.LocationGroupDTO;
import com.jack.location.response.BaseResponse;

import java.util.List;

public interface LocationGroupService {
    BaseResponse createOrUpdateLocationGroup(List<LocationGroupDTO> changes);
}
