package com.jack.location.dao.repository;

import com.jack.location.dao.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, String> {
    List<LocationEntity> findAllByCodeIn(Collection<String> codes);
}
