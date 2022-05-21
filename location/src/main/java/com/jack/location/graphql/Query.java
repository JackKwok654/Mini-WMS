package com.jack.location.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.jack.location.dao.projection.WarehouseProjection;
import com.jack.location.dao.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class Query implements GraphQLQueryResolver {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Transactional
    public Iterable<WarehouseProjection> getAllWarehouse() {
        return warehouseRepository.getAllProjection();
    }
}
