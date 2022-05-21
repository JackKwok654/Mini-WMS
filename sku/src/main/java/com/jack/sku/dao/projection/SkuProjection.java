package com.jack.sku.dao.projection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface SkuProjection {
    String getCode();

    String getName();

    String getDescription();

    Double getLength();

    Double getWidth();

    Double getHeight();
}
