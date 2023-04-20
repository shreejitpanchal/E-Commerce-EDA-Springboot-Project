package com.ms.orderservice.api.service.createorder;

import com.ms.orderservice.api.model.CreateOrderAPIRequest;
import com.ms.orderservice.api.model.CreateOrderAPIResponse;

public interface CreateOrder {
    CreateOrderAPIResponse createOrder(CreateOrderAPIRequest apiRequest);
}
