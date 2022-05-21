package com.jack.location.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "location_zone")
@EqualsAndHashCode(callSuper = false)
public class ZoneEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, insertable = false)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_code", nullable = false)
    private WarehouseEntity warehouse;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LocationGroupEntity> locationGroups;

    public String getWarehouseCode() {
        if (warehouse != null) {
            return warehouse.getCode();
        } else {
            return null;
        }
    }
}
