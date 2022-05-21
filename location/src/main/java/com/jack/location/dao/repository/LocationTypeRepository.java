package com.jack.location.dao.repository;

import com.jack.location.dao.entity.LocationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LocationTypeRepository extends JpaRepository<LocationTypeEntity, String> {
    List<LocationTypeEntity> findAllByCodeIn(Collection<String> codes);
}
