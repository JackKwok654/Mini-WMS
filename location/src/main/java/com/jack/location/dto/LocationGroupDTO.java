package com.jack.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationGroupDTO {
    private String code;

    private String zoneCode;

    private String warehouseCode;

    private Integer priority;
}
