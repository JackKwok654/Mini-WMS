package com.jack.sku.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "sku_barcode")
@EqualsAndHashCode(callSuper = false)
public class BarcodeEntity extends BaseEntity {
    @Id
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    /*  JPA 2.0 spec, the defaults are:
        OneToMany: LAZY
        ManyToOne: EAGER
        ManyToMany: LAZY
        OneToOne: EAGER
    */
    // Owning side, owns the foreign key in the database
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sku_code", nullable = false)
    private SkuEntity skuEntity;
}
