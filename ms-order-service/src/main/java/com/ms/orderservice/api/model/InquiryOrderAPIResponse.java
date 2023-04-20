package com.ms.orderservice.api.model;

import com.ms.orderservice.api.entity.OrderService;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InquiryOrderAPIResponse {

    private List<OrderService> orderService;
}
