package com.jack.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneDTO {
    private String code;

    private Integer priority;

    private String warehouseCode;
}
