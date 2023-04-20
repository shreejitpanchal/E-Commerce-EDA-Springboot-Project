package com.ms.orderservice.api.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateOrderRequest {
    private String orderId;
    private String customerName;

    private String orderStatus;
    private String orderDesc;

}
