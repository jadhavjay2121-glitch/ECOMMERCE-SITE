package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByOrderByCreatedAtDesc();
}
