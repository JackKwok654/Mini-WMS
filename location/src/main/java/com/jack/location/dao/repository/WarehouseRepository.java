package com.jack.location.dao.repository;

import com.jack.location.dao.entity.WarehouseEntity;
import com.jack.location.dao.projection.WarehouseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseEntity, String> {
    List<WarehouseEntity> findAllByCodeIn(Collection<String> codes);

    @Query(value = "SELECT  " +
            "    code, name, description " +
            "FROM " +
            "    warehouse ",
            nativeQuery = true)
    List<WarehouseProjection> getAllProjection();
}
