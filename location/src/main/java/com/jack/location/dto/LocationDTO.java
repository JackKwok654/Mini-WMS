package com.jack.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    private String code;

    private String typeCode;

    private String warehouseCode;

    private String zoneCode;

    private String groupCode;

    private Integer priority;
}
