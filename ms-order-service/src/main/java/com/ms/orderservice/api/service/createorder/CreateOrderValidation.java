package com.ms.orderservice.api.service.createorder;

import com.ms.orderservice.api.entity.OrderService;
import com.ms.orderservice.api.model.CreateOrderAPIRequest;
import com.ms.orderservice.api.model.CreateOrderAPIResponse;
import com.ms.orderservice.api.repository.OrderServiceRepository;
import com.ms.orderservice.api.service.commonservices.PublishOrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Validation class for creating order
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class CreateOrderValidation {

    Logger logger = LoggerFactory.getLogger(CreateOrderValidation.class);
    @Value("#{'${eda.order.field.mobile.types}'.split(',')}")
    private List<String> mobileTypes;

    public boolean validateInput(CreateOrderAPIRequest apiRequest) {
        if (!mobileTypes.contains(apiRequest.getOrderRequest().getProductType().toLowerCase(Locale.ROOT))) {
            logger.info("Invalid MobileType");
            return true;
        }
        return false;
    }

}
