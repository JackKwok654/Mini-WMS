package com.jack.sku.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.jack.sku.dao.repository.SkuRepository;
import com.jack.sku.dao.projection.SkuProjection;
import com.jack.sku.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class Query implements GraphQLQueryResolver {
    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuRepository skuRepository;

    @Transactional
    public Iterable<SkuProjection> getAllSku() {
        log.info("[getAllSku] Starting query...");
        List<SkuProjection> result = skuRepository.findAllSkuProjection();
        log.info("[getAllSku] Finished query, result size: {}", result.size());
        return result;
    }
}
