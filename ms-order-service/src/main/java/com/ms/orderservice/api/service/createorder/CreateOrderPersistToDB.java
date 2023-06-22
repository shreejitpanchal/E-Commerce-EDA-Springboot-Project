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

/**
 * Persist Records to DB creating order
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class CreateOrderPersistToDB {

    Logger logger = LoggerFactory.getLogger(CreateOrderPersistToDB.class);

//    @Autowired
//    private CreateOrderAPIResponse apiResponse;
    @Autowired
    private OrderServiceRepository orderServiceRepository;
    @Autowired
    private PublishOrderEvent publishOrderEvent;

    @Value("{'${eda.order.initial.status.value}")
    private String initialStatusValue;

    public boolean saveOrderRecord(CreateOrderAPIRequest apiRequest, CreateOrderAPIResponse.orderResponse orderResponseDtl, String orderId, LocalDateTime CurrrentDateTime) {
        try {
            logger.info("Step-2 === Init OrderService DB === Start");

            orderServiceRepository.save(OrderService.builder()
                    .orderId(orderId)
                    .userId(apiRequest.getOrderRequest().getUserId())
                    .customerName(apiRequest.getOrderRequest().getCustomerName())
                    .productId(apiRequest.getOrderRequest().getProductId())
                    .productDesc(apiRequest.getOrderRequest().getProductDesc())
                    .quantity(apiRequest.getOrderRequest().getQuantity())
                    .orderDateTime(apiRequest.getOrderRequest().getOrderDateTime())
                    .createdDateTime(ZonedDateTime.parse(CurrrentDateTime.toString()))
                    .createdBy("API")
                    .orderStatus(initialStatusValue)
                    .orderDesc("Published Event to EDA")
                    .correlationId(apiRequest.getCorrelationId())
                    .transactionId(apiRequest.getTransactionId())
                    .build());
            logger.info("Step-2.1 === Init OrderService DB === End");
            boolean response = publishOrderEvent.sendEvent(apiRequest, orderId);
            if (!response){
                logger.error("Step-2.2 === createOrder Publish Method return false");
                return true;
            }

        } catch (Exception e) {
            logger.error("Step-2-Err === createOrder InvokeEDA Method Error ==> error:" + e.getMessage());
        }
        return false;
    }

}
