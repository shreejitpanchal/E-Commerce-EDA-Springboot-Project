package com.ms.orderservice.api.service.inquiryorder;

import com.ms.orderservice.api.model.InquiryOrderAPIRequest;
import com.ms.orderservice.api.model.InquiryOrderAPIResponse;

public interface InquiryOrder {
    InquiryOrderAPIResponse inquiryOrder(InquiryOrderAPIRequest apiResponse);
}
