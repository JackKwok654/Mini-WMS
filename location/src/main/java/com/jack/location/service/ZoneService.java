package com.jack.location.service;

import com.jack.location.dto.ZoneDTO;
import com.jack.location.response.BaseResponse;

import java.util.List;

public interface ZoneService {
    BaseResponse createOrUpdateZone(List<ZoneDTO> changes);
}
