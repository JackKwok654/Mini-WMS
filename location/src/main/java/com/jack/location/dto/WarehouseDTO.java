package com.jack.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDTO {
    private String code;

    private String name;

    private String description;
}
