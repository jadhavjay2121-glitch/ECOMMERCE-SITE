package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Default dashboard sorting
    List<Order> findAllByOrderByCreatedAtDesc();

    // Filter sorting for dashboard based on order status drop-down
    @Query("SELECT o FROM Order o WHERE (:filterStatus IS NULL OR :filterStatus = '' OR o.orderStatus = :filterStatus) ORDER BY o.createdAt DESC")
    List<Order> findByOrderStatusFilter(@Param("filterStatus") String filterStatus);
}
