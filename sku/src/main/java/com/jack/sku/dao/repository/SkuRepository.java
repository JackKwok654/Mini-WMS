package com.jack.sku.dao.repository;

import com.jack.sku.dao.entity.SkuEntity;
import com.jack.sku.dao.projection.SkuProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SkuRepository extends JpaRepository<SkuEntity, String> {
    @Query(value = "SELECT  " +
            "    sku.code, " +
            "    sku.name, " +
            "    sku.description, " +
            "    sku.length, " +
            "    sku.width, " +
            "    sku.height " +
            "FROM " +
            "    sku ",
            nativeQuery = true)
    List<SkuProjection> findAllSkuProjection();

    List<SkuEntity> findAllByCodeIn(Collection<String> codes);
}
