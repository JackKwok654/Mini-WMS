package com.jack.sku.dao.repository;

import com.jack.sku.dao.entity.BarcodeEntity;
import com.jack.sku.dao.projection.BarcodeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BarcodeRepository extends JpaRepository<BarcodeEntity, String> {
    @Query(value = "SELECT  " +
            "    sb.sku_code AS skuCode, sb.barcode " +
            "FROM " +
            "    sku_barcode AS sb " +
            "WHERE " +
            "    sb.sku_code = :skuCode ",
            nativeQuery = true)
    List<BarcodeProjection> findBarcodeProjectionBySkuCode(String skuCode);

    @Query(value = "SELECT  " +
            "    sb.sku_code AS skuCode, sb.barcode " +
            "FROM " +
            "    sku_barcode AS sb " +
            "WHERE " +
            "    sb.barcode IN :barcodes ",
            nativeQuery = true)
    List<BarcodeProjection> findBarcodeProjectionInBarcodes(Collection<String> barcodes);

    List<BarcodeEntity> findAllByBarcodeIn(Collection<String> barcodes);
}
