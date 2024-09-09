package com.cookerytech.repository;

import com.cookerytech.domain.Cart;
import com.cookerytech.domain.Cart_Items;
import com.cookerytech.domain.Model;
import com.cookerytech.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<Cart_Items,Long> {

    @Query("SELECT ci FROM Cart_Items ci JOIN ci.product pr WHERE pr.id = :productId")
    List<Cart_Items> getCartItemsByProductId(@Param("productId") Long productId);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Cart getir(@Param("userId") Long userId);

    @Query("SELECT c FROM Cart_Items c WHERE c.cart.id = :id")
    List<Cart_Items> getirCartItems(@Param("id") Long id);

    @Query("SELECT c FROM Cart_Items c WHERE c.cart.user = :user and c.model.id = :modelId")
    Optional<Cart_Items> getCartItemsByModelIdandByUser(@Param("modelId") Long modelId, @Param("user") User user);

    List<Cart_Items> findAllByModel(Model model);
    @EntityGraph(attributePaths = {"model", "product"})
    @Query("SELECT ci FROM Cart_Items ci WHERE ci.cart.user.id = :userId")
    List<Cart_Items> getCartItemsByUserId(@Param("userId")Long userId);
}
