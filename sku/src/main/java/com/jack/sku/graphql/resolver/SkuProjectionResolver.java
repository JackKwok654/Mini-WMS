package com.jack.sku.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.jack.sku.dao.projection.BarcodeProjection;
import com.jack.sku.dao.projection.SkuProjection;
import com.jack.sku.dao.repository.BarcodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class SkuProjectionResolver implements GraphQLResolver<SkuProjection> {
    @Autowired
    private BarcodeRepository barcodeRepository;

    @Transactional
    public List<BarcodeProjection> getBarcodes(SkuProjection sku) {
        return barcodeRepository.findBarcodeProjectionBySkuCode(sku.getCode());
    }
}
