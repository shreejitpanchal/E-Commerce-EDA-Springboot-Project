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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
    @Value("#{'${eda.order.mobiletypes}'.split(',')}")
    private List<String> mobileTypes;

    @Override
    public CreateOrderAPIResponse createOrder(CreateOrderAPIRequest apiRequest) {

        logger.info("Create API === createOrder Request ==> Start");
        logger.info("Step-1 === CustomerName: " + apiRequest.getOrder().getCustomerName()+ " ProductDesc: " + apiRequest.getOrder().getProductDesc());

        if (validateInput(apiRequest)) {
            logger.info("Step-1-Err === createOrder MobileType validation Error ==> End");
            return CreateOrderAPIResponse.builder().orderStatus("Error").orderDesc("Invalid MobileType").build();
        }

        try {
            logger.info("Step-2 === Init DB === Start");
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            orderId = formatter.format(new Date()) + (new Random().nextInt(5000));
            orderServiceRepository.save(OrderService.builder().orderId(orderId).customerName(apiRequest.getCustomerName()).mobileType(apiRequest.getMobileType()).orderDateTime(apiRequest.getOrderDateTime()).orderStatus("Initiated").orderDesc("Pending EDA Confirmation").build());
            logger.info("Step-2.1 === Init DB === End");
            publishOrderEvent.sendEvent(apiRequest, orderId);
        } catch (Exception e) {
            logger.error("Step-2-Err === createOrder InvokeEDA Method Error ==> error:" + e.getMessage());
        }

        logger.info("Step-3 === createOrder Successful ==> End");
        return CreateOrderAPIResponse.builder().orderId(orderId).customerName(apiRequest.getCustomerName()).mobileType(apiRequest.getMobileType()).orderDateTime(apiRequest.getOrderDateTime()).orderStatus("Initiated").orderDesc("Pending EDA Confirmation").build();
    }

    private boolean validateInput(CreateOrderAPIRequest apiRequest) {
        if (!mobileTypes.contains(apiRequest.getOrder().getProductType().toLowerCase(Locale.ROOT))) {
            logger.info("Invalid MobileType");
            return true;
        }
        return false;
    }
}
