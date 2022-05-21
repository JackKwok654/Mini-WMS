package com.jack.location.dao.repository;

import com.jack.location.dao.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseEntity, String> {
    List<WarehouseEntity> findAllByCodeIn(Collection<String> codes);
}
