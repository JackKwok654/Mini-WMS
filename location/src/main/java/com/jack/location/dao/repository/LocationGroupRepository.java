package com.jack.location.dao.repository;

import com.jack.location.dao.entity.LocationGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LocationGroupRepository extends JpaRepository<LocationGroupEntity, String> {
    List<LocationGroupEntity> findAllByCodeIn(Collection<String> codes);
}
