package com.ms.orderservice.api.model;

import lombok.Getter;
import org.springframework.stereotype.Component;


@Component
@Getter
public class InquiryOrderAPIRequest {
    private String customerName;

}
