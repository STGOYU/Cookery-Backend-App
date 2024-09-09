package com.cookerytech.repository;

import com.cookerytech.domain.Currency;
import com.cookerytech.domain.Offer;
import com.cookerytech.domain.User;
import com.cookerytech.domain.enums.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o " +
            "WHERE (" +
             "(  :q IS NULL " +
                 "OR o.code LIKE %:q% " +
                 "OR o.user.firstName LIKE %:q% " +
                 "OR o.user.lastName LIKE %:q%" +
             ") " +
             "AND (:status IS NULL OR o.status = :status) " +
             "AND (COALESCE(:date1, CAST('1970-01-01T00:00:00' AS timestamp)) <= o.createAt) " +
             "AND (COALESCE(:date2, CAST('2100-12-31T23:59:59' AS timestamp)) >= o.createAt)" +
            ") ")
    Page<Offer> findFilteredOffers(@Param("q") String query,
                                   @Param("status") OfferStatus status,
                                   @Param("date1") LocalDateTime date1,
                                   @Param("date2") LocalDateTime date2,
//                                   @Param("minDate") LocalDateTime minDate,
//                                   @Param("maxDate") LocalDateTime maxDate,
                                   Pageable pageable);

//    @Query("SELECT o FROM Offer o " +
//            "WHERE (o.code LIKE %:q% " +
//            "OR o.user.firstName LIKE %:q% " +
//            "OR o.user.lastName LIKE %:q%) " +
//            "AND (:status IS NULL OR o.status = :status) " +
//            "AND (:date1 IS NULL OR o.createAt >= :date1) " +
//            "AND (:date2 IS NULL OR o.createAt <= :date2)")




    Optional<Offer> findByIdAndUser(Long id, User user);


    List<Offer> findAllByUserId(Long id);

    @Query("SELECT o FROM Offer o where  lower(o.id) like %?1% " +
                                                     " OR lower(o.code) like %?1% " +
                                                     " OR (lower(o.createAt) BETWEEN ?2 AND ?3) " +
                                                     " OR lower(o.status) like %?4%")
    Page<Offer> getAllOffers(String qLower, String date1, String date2, String statusLower, Pageable pageable);

    @Query("SELECT o FROM Offer o where (o.createAt BETWEEN ?1 AND ?2) " +
            " AND o.status=?3 AND o.user=?4")
    Page<Offer> getAllOffersByUser(LocalDateTime date1, LocalDateTime date2, OfferStatus status, User user, Pageable pageable);

    @Query("SELECT o FROM Offer o")
    Page<Offer> findAllOffersWithPage(Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE " +
            "o.user.id=?2")
    Page<Offer> findAllOffersWithPageByUser(Pageable pageable,User user);

//    @EntityGraph(attributePaths = "offer")
//    @Query("SELECT oi FROM OfferItem oi LEFT JOIN FETCH oi.offer WHERE oi.offer=: offerId ")
//    Optional<Offer> findByOfferId(@Param("offerId") Long id);

    Boolean existsByCode(String code);

    @Query("SELECT o FROM Offer o WHERE o.createAt >= :startTime")
    List<Offer> offersPerDay(@Param("startTime") LocalDateTime startTime);

    @Modifying
    @Query("UPDATE Offer o SET o.grandTotal = :grandTotal WHERE o.id = :offerId")
    void updateGrandTotal(@Param("offerId") Long offerId,@Param("grandTotal") Double grandTotal);

    @Query("SELECT o FROM Offer o WHERE o.createAt BETWEEN ?1 AND ?2")
    List<Offer> getAllOffersBetweenDate(LocalDateTime dateTime1, LocalDateTime dateTime2);

    Boolean existsByUser(User user);
}
