package com.jack.sku.dao.entity;

import com.jack.sku.dto.SkuDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "sku")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SkuEntity extends BaseEntity {
    @Id
    @Column(name = "code", nullable = false, unique = true, length = 45)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @OneToMany(mappedBy = "skuEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BarcodeEntity> barcodes;

    public SkuEntity(SkuDTO skuDTO)
    {
        code = skuDTO.getCode();
        name = skuDTO.getName();
        description = skuDTO.getDescription();
        length = skuDTO.getLength();
        width = skuDTO.getWidth();
        height = skuDTO.getHeight();
    }
}
