package com.cookerytech.repository;

import com.cookerytech.domain.Favorite;
import com.cookerytech.domain.Model;
import com.cookerytech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Favorite findByModelAndUser(Model model, User authenticatedUser);

    @Query("SELECT f FROM Favorite f JOIN f.user u WHERE u.id = :userId")
    List<Favorite> findAllByUserId(@Param("userId") Long userId);

//    @Query("SELECT f FROM Favorite f JOIN f.productId pr WHERE pr.id = :productId")
//    List<Favorite> getFavoritesByProductId(@Param("productId") Long productId);
    @Query("SELECT f FROM Favorite f JOIN f.model m WHERE m.product.id = :productId")
    List<Favorite> getFavoritesByModelsOfProduct(Long productId);


    List<Favorite> findAllByUser(User currentlyUser);

    List<Favorite> findAllByModel(Model model);

    boolean existsByModelAndUser(Model model, User user);
}
