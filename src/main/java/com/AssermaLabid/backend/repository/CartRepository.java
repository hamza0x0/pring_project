package com.AssermaLabid.backend.repository;

import com.AssermaLabid.backend.models.Cart;
import com.AssermaLabid.backend.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.items WHERE c.id = :id")
    Cart findByIdWithItems(@Param("id") Long id);
    @Query("select c.items from Cart c where c.id = :cartId")
    List<CartItem> getCartItemsByCartId(@Param("cartId") Long cartId);

}
