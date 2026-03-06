package com.ecommerce.repository;

import com.ecommerce.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByOrderByCreatedAtDesc();

    Coupon findByCouponCode(String couponCode);
}
