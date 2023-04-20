package com.ms.orderservice.api.repository;

import com.ms.orderservice.api.entity.OrderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderService, BigInteger> {

    @Query(value = "SELECT * FROM order_service where order_id=?1", nativeQuery = true)
    Optional<OrderService> findCustomerByOrderId(String orderId);

    @Query(value = "SELECT * FROM order_service where UPPER(customer_name)=?1", nativeQuery = true)
    List<OrderService> findCustomerByCustomerName(String customer_name);

    @Query(value = "SELECT * FROM order_service", nativeQuery = true)
    List<OrderService> findAllCustomer();
}
