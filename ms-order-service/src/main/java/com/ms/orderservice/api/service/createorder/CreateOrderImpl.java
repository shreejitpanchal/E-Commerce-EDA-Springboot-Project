package com.ms.orderservice.api.service.createorder;

import com.ms.orderservice.api.service.commonservices.PublishOrderEvent;
import com.ms.orderservice.api.entity.OrderService;
import com.ms.orderservice.api.model.CreateOrderAPIRequest;
import com.ms.orderservice.api.model.CreateOrderAPIResponse;
import com.ms.orderservice.api.repository.OrderServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.time.LocalDateTime;

/**
 * Create Order Service for creating order
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class CreateOrderImpl implements CreateOrder {

    Logger logger = LoggerFactory.getLogger(CreateOrderImpl.class);
    String orderId;
    @Autowired
    private CreateOrderAPIResponse apiResponse;
    @Autowired
    private OrderServiceRepository orderServiceRepository;
    @Autowired
    private PublishOrderEvent publishOrderEvent;
    @Value("#{'${eda.order.field.mobile.types}'.split(',')}")
    private List<String> mobileTypes;

    @Value("#{'${eda.order.initial.status.value}")
    private String initialStatusValue;

    @Override
    public CreateOrderAPIResponse createOrder(CreateOrderAPIRequest apiRequest) {

        logger.info("API === createOrder Request ==> Start");
        logger.info("Step-1 === CustomerName: " + apiRequest.getOrder().getCustomerName()+ " ProductDesc: " + apiRequest.getOrder().getProductDesc());
        CreateOrderAPIResponse.order orderDtl=new CreateOrderAPIResponse.order();

        LocalDateTime CurrrentDateTime = LocalDateTime.now();

        if (validateInput(apiRequest)) {
            logger.info("Step-1-Err === createOrder MobileType validation Error ==> End");
            return CreateOrderAPIResponse.builder()
                    .correlationId(apiRequest.getCorrelationId())
                    .order(orderDtl.builder()
                            .orderStatus("Error")
                            .orderDesc("Invalid MobileType")
                            .build())
                    .build();
        }

        try {
            logger.info("Step-2 === Init OrderService DB === Start");
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            orderId = formatter.format(new Date()) + (new Random().nextInt(5000));
            orderServiceRepository.save(OrderService.builder()
                    .orderId(orderId)
                    .userId(apiRequest.getOrder().getUserId())
                    .customerName(apiRequest.getOrder().getCustomerName())
                    .productId(apiRequest.getOrder().getProductId())
                    .productDesc(apiRequest.getOrder().getProductDesc())
                    .quantity(apiRequest.getOrder().getQuantity())
                    .orderDateTime(apiRequest.getOrder().getOrderDateTime())
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
                return CreateOrderAPIResponse.builder()
                        .order(orderDtl.builder().orderStatus("Error").build())
                        .correlationId(apiRequest.getCorrelationId())
                        .build();
            }

        } catch (Exception e) {
            logger.error("Step-2-Err === createOrder InvokeEDA Method Error ==> error:" + e.getMessage());
        }

        logger.info("Step-3 === createOrder Successful ==> End");
        return CreateOrderAPIResponse.builder()
                .order(orderDtl.builder()
                        .orderId(orderId)
                        .customerName(apiRequest.getOrder().getCustomerName())
                        .productId(apiRequest.getOrder().getProductId())
                        .orderDateTime(apiRequest.getOrder().getOrderDateTime())
                        .orderStatus(initialStatusValue)
                        .orderDesc("Published Event to EDA")
                        .build())
                        .build();
    }

    private boolean validateInput(CreateOrderAPIRequest apiRequest) {
        if (!mobileTypes.contains(apiRequest.getOrder().getProductType().toLowerCase(Locale.ROOT))) {
            logger.info("Invalid MobileType");
            return true;
        }
        return false;
    }
}
