package com.cookerytech.repository;

import com.cookerytech.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :categoryId AND p.isActive = true")
    List<Product> getProductsByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p JOIN p.brand b WHERE b.id = :brandId")
    List<Product> findProductByBrandId(@Param("brandId") Long brandId);

    /*
    @Query("SELECT p FROM Product p\n" +
            "JOIN p.brand b\n" +
            "JOIN p.category c\n" +
            "WHERE b.isActive = true AND c.isActive = true AND p.isActive = true OR p.title = ?1")
    Page<Product> getActiveProducts(String q, Pageable pageable);

     */
/*
    @Query("SELECT p FROM Product p JOIN p.brand b JOIN p.category c " +
            "WHERE b.isActive = true " +
            " AND c.isActive = true " +
            " AND p.isActive = true" +
            " AND p.isFeatured = true")
    List<Product> getAllFeaturedProducts();

 */

    @Query("SELECT p FROM Product p LEFT JOIN OfferItem o ON p.id=o.product.id WHERE o.product.id IS NULL")
    List<Product> getProductsNoOffer();


    @Query("SELECT p FROM Product p  JOIN OfferItem o ON p.id=o.product.id GROUP BY p.id ORDER BY COUNT(o) DESC ")
    List<Product> getMostPopularProducts();

    @Query("SELECT p FROM Product p WHERE p.isFeatured = true")
    List<Product> getAllFeaturedProductsForAdmin();

    @Query("SELECT count(p) FROM Product p WHERE p.isActive = true")
    long numberOfPublishedProduct();
}
