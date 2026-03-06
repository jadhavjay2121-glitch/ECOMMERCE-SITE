package com.ecommerce.repository;

import com.ecommerce.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    List<Shipping> findAllByOrderByCreatedAtDesc();
}
