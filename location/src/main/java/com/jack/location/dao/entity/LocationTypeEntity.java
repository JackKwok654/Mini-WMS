package com.jack.location.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "location_type")
@EqualsAndHashCode(callSuper = false)
public class LocationTypeEntity extends BaseEntity {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "frozen", nullable = false)
    private Boolean frozen;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @OneToMany(mappedBy = "locationType", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<LocationEntity> locations;
}
