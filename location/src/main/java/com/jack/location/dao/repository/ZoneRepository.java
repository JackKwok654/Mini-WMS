package com.jack.location.dao.repository;

import com.jack.location.dao.entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {
    @Query(value = "SELECT  " +
            "    * " +
            "FROM " +
            "    location_zone " +
            "WHERE " +
            "    warehouse_code = :warehouseCode " +
            "        AND code IN (:codes) ",
            nativeQuery = true)
    List<ZoneEntity> findAllByWarehouseCodeIsAndCodeIn(String warehouseCode, Collection<String> codes);
}
