package com.cookerytech.repository;

import com.cookerytech.domain.ProductPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPropertyKeyRepository extends JpaRepository<ProductPropertyKey, Long> {
    @Query("SELECT p FROM ProductPropertyKey p JOIN p.product pr WHERE pr.id = :productId")
    List<ProductPropertyKey> findAllByProductId(@Param("productId") Long productId);

    @Query("SELECT p FROM ProductPropertyKey p WHERE p.product.id = :productId AND p.name = :name")
    ProductPropertyKey findByProductPropertyKey(@Param("name") String name, @Param("productId") Long productId);
}
