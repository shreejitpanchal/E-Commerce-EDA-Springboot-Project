package com.ms.orderservice.api.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;


@Component
@Getter
public class CreateOrderAPIRequest {

    private String correlationId;
    private String transactionId;
    private orderRequest orderRequest;

    @Getter
    @Setter
    public class orderRequest {
        @NotNull
        private String userId;
        private String customerName;
        private String productId;
        @NotNull(message="Possible value Samsung or IPhone")
        private String productType;
        private String productDesc;
        private String quantity;
        private ZonedDateTime orderDateTime;
    }
}
