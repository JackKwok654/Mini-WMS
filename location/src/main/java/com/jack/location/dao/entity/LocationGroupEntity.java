package com.jack.location.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "location_group")
@EqualsAndHashCode(callSuper = false)
public class LocationGroupEntity extends BaseEntity {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id", nullable = false)
    private ZoneEntity zone;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @OneToMany(mappedBy = "locationGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LocationEntity> locations;
}
