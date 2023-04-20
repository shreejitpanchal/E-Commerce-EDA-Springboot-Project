package com.ms.orderservice.api.service.updateorder;

import com.ms.orderservice.api.model.CreateOrderAPIResponse;
import com.ms.orderservice.api.model.UpdateOrderRequest;

public interface UpdateOrder {
    CreateOrderAPIResponse updateOrder(UpdateOrderRequest apiResponse);
}
