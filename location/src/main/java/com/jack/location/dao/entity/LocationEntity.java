package com.jack.location.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "location")
@EqualsAndHashCode(callSuper = false)
public class LocationEntity extends BaseEntity {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_code", nullable = false)
    private LocationTypeEntity locationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_code", nullable = false)
    private LocationGroupEntity locationGroup;

    @Column(name = "priority", nullable = false)
    private Integer priority;
}
