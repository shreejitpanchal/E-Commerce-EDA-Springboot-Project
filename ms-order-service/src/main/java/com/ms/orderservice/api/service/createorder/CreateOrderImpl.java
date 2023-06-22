package com.ms.orderservice.api.service.createorder;

import com.ms.orderservice.api.service.commonservices.PublishOrderEvent;
import com.ms.orderservice.api.model.CreateOrderAPIRequest;
import com.ms.orderservice.api.model.CreateOrderAPIResponse;
import com.ms.orderservice.api.repository.OrderServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private CreateOrderPersistToDB createOrderPersistToDB;

    @Autowired
    private CreateOrderValidation createOrderValidation;
    @Value("#{'${eda.order.field.mobile.types}'.split(',')}")
    private List<String> mobileTypes;

    @Value("{'${eda.order.initial.status.value}")
    private String initialStatusValue;

    @Override
    public CreateOrderAPIResponse createOrder(CreateOrderAPIRequest apiRequest) {

        logger.info("API === createOrder Request ==> Start");
        logger.info("Step-1 === CustomerName: " + apiRequest.getOrderRequest().getCustomerName()+ " ProductDesc: " + apiRequest.getOrderRequest().getProductDesc());
        CreateOrderAPIResponse.orderResponse orderResponseDtl =new CreateOrderAPIResponse.orderResponse();

        LocalDateTime currrentDateTime = LocalDateTime.now();

        if (createOrderValidation.validateInput(apiRequest)) {
            logger.info("Step-1-Err === createOrder MobileType validation Error ==> End");
            return CreateOrderAPIResponse.builder()
                    .correlationId(apiRequest.getCorrelationId())
                    .orderResponse(orderResponseDtl.builder()
                            .orderStatus("Error")
                            .orderDesc("Invalid MobileType")
                            .build())
                    .build();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        orderId = formatter.format(new Date()) + (new Random().nextInt(5000));

        if (createOrderPersistToDB.saveOrderRecord(apiRequest, orderResponseDtl,orderId, currrentDateTime)) return CreateOrderAPIResponse.builder()
                .orderResponse(orderResponseDtl.builder().orderStatus("Error").build())
                .correlationId(apiRequest.getCorrelationId())
                .build();

        logger.info("Step-3 === createOrder Successful ==> End");
        return CreateOrderAPIResponse.builder()
                .orderResponse(orderResponseDtl.builder()
                        .orderId(orderId)
                        .customerName(apiRequest.getOrderRequest().getCustomerName())
                        .productId(apiRequest.getOrderRequest().getProductId())
                        .orderDateTime(apiRequest.getOrderRequest().getOrderDateTime())
                        .orderStatus(initialStatusValue)
                        .orderDesc("Published Event to EDA")
                        .build())
                        .build();
    }

}
