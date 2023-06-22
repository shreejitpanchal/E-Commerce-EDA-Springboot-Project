package com.ms.orderservice.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;


@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EDAPublishOrderEventRequest {

    private String correlationId;
    private String transactionId;
    private order order;

    @Getter
    @Setter
    @Builder
    @Component
    @NoArgsConstructor
    @AllArgsConstructor
    public static class order {
        private String userId;
        private String orderId;
        private String customerName;
        private String productId;
        private String productType;
        private String productDesc;
        private String quantity;

        @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSS", locale = "en_SG", timezone = "Asia/Singapore")
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        private ZonedDateTime orderDateTime;
    }

}
