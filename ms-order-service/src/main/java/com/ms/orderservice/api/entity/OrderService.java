package com.ms.orderservice.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_service")
public class OrderService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger Id;

    @Column(name="order_id")
    private String orderId;

    @Column(name="user_id")
    private String userId;

    @Column(name="customer_name")
    private String customerName;

    @Column(name="product_id")
    private String productId;

    @Column(name="product_type")
    private String productType;

    @Column(name="product_desc")
    private String productDesc;

    @Column(name="quantity")
    private String quantity;

    @Column(name="order_status")
    private String orderStatus;

    @Column(name="order_desc")
    private String orderDesc;

    @Column(name="correlation_id")
    private String correlationId;

    @Column(name="transaction_id")
    private String transactionId;

    @Column(name="order_datetime")
    private ZonedDateTime orderDateTime;

    @Column(name="created_datetime")
    private ZonedDateTime createdDateTime;

    @Column(name="modified_datetime")
    private ZonedDateTime modifiedDateTime;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="modified_by")
    private String modifiedBy;
}
