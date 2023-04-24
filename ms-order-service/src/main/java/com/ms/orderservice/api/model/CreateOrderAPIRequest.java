package com.ms.orderservice.api.model;

import lombok.Getter;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
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
