package com.cookerytech.repository;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.ModelPropertyValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    @Query("SELECT m FROM Model m WHERE m.isActive = true")
    Page<Model> getActiveModels(Pageable pageable);

    Boolean existsBySku(String sku);

    @Query("Select m from Model m  where m.product.id=:productId")
    List<Model> findAllByProductId(@Param("productId") Long productId);

    @Query("SELECT m FROM Model m WHERE m.product.id=:productId and m.isActive=true" +
            " and m.product.isActive = true " +
            "and m.product.category.isActive=true and m.product.brand.isActive=true ")
    List<Model> getModelsByProductIdActiveModelBrandCategoryProduct(@Param("productId") Long productId);


}
