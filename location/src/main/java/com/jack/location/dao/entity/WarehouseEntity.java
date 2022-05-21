package com.jack.location.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "warehouse")
@EqualsAndHashCode(callSuper = false)
public class WarehouseEntity extends BaseEntity {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ZoneEntity> zones;
}
