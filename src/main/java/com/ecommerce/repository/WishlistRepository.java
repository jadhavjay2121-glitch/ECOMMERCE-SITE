package com.ecommerce.repository;

import com.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAllByOrderByCreatedAtDesc();
}
