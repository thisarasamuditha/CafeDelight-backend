package com.thisara.cafe.repository;

import com.thisara.cafe.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}
