package com.ms.orderservice.api.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrderAPIResponse {

    private String correlationId;
    private String transactionId;
    private orderResponse orderResponse;

    @Component
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class orderResponse {
        private String orderId;
        private String customerName;
        private String productId;
        private ZonedDateTime orderDateTime;
        private String orderStatus;
        private String orderDesc;
    }
}
