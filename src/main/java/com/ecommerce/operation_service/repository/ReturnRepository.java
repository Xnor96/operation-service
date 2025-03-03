package com.ecommerce.operation_service.repository;

import com.ecommerce.operation_service.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Long> {
    List<Return> findByOrderId(Long orderId);
}