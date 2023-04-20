package com.ms.orderservice.api.model;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;


@Component
@Getter
public class CreateOrderAPIRequest {

    private String correlationId;
    private String transactionId;
    private order order;

    @Component
    @Getter
    public class order {
        private String userId;
        private String customerName;
        private String productId;
        private String productType;
        private String productDesc;
        private String quantity;
        private ZonedDateTime orderDateTime;
    }
}
