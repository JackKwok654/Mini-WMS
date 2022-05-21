package com.jack.location.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Column(name = "creation_date", insertable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_modified_date", insertable = false, updatable = false)
    private LocalDateTime lastModifiedDate;
}
