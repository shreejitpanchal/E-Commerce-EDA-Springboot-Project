package com.ms.orderservice.api.service.updateorder;

import com.ms.orderservice.api.service.commonservices.PublishOrderEvent;
import com.ms.orderservice.api.entity.OrderService;
import com.ms.orderservice.api.model.CreateOrderAPIResponse;
import com.ms.orderservice.api.model.UpdateOrderRequest;
import com.ms.orderservice.api.repository.OrderServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * update Order Service for update order
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class UpdateOrderImpl implements UpdateOrder {

    Logger logger = LoggerFactory.getLogger(UpdateOrderImpl.class);

    @Autowired
    private OrderServiceRepository orderServiceRepository;

    @Autowired
    private PublishOrderEvent publishOrderEvent;
    @Autowired
    private Optional<OrderService> customerMasterDTO;

    @Override
    public CreateOrderAPIResponse updateOrder(UpdateOrderRequest apiRequest) {
        logger.info("Update API === updateOrder Request ==> Start");
        logger.info("Step-1 === Customer Name:" + apiRequest.getCustomerName(), " Order Status:" + apiRequest.getOrderStatus());

        try {
            customerMasterDTO = orderServiceRepository.findCustomerByOrderId(apiRequest.getOrderId());
            logger.info("Step-2 === Database record located - customerMasterDTO.isPresentFlag =" + customerMasterDTO.isPresent());
            customerMasterDTO.get().setOrderStatus(apiRequest.getOrderStatus());
            customerMasterDTO.get().setOrderDesc(apiRequest.getOrderDesc());
            orderServiceRepository.save(customerMasterDTO.get());

            logger.info("Step-3 === updateOrder Successful ==> End");

        } catch (Exception e) {
            logger.info("Step-2.Error === Error in Update Customer Master IMPL :" + e.getMessage());
        }

        return CreateOrderAPIResponse.builder().build();
    }

}
