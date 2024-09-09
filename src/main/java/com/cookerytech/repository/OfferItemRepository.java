package com.cookerytech.repository;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.OfferItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem, Long> {


    @EntityGraph(attributePaths = {"offerId"})
    List<OfferItem> findByOfferId(Long offerId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OfferItem o WHERE o.product.id = :productId")
    Boolean existsByProductId(@Param("productId") Long productId);

//    @EntityGraph(attributePaths = {"id"})
    @Query("SELECT oi FROM OfferItem oi WHERE oi.offer.id = :offerId")
    List<OfferItem> getByOfferItemByOfferId(@Param("offerId") Long offerId);


    Boolean existsByModel(Model model);
}
