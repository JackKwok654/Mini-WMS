package com.jack.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationTypeDTO {
    private String code;

    private Boolean frozen;

    private Double length;

    private Double width;

    private Double height;
}
