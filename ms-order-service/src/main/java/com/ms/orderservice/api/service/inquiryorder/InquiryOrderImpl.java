package com.ms.orderservice.api.service.inquiryorder;

import com.ms.orderservice.api.service.commonservices.PublishOrderEvent;
import com.ms.orderservice.api.entity.OrderService;
import com.ms.orderservice.api.model.InquiryOrderAPIRequest;
import com.ms.orderservice.api.model.InquiryOrderAPIResponse;
import com.ms.orderservice.api.repository.OrderServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Inquiry Order Service for Inquire based on Customer Name
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class InquiryOrderImpl implements InquiryOrder {

    Logger logger = LoggerFactory.getLogger(InquiryOrderImpl.class);

    @Autowired
    private OrderServiceRepository orderServiceRepository;

    @Autowired
    private PublishOrderEvent publishOrderEvent;
    @Autowired
    private Optional<OrderService> customerMasterDTO;

    @Override
    public InquiryOrderAPIResponse inquiryOrder(InquiryOrderAPIRequest apiRequest) {
        logger.info("Inquiry API === inquiryOrder Request ==> Start");
        logger.info("Step-1 === Customer Name:" + apiRequest.getCustomerName());

        //List<CustomerMaster> customerMasterCollector = customerMasterRepository.findCustomerByCustomerName(apiRequest.getCustomerName().toUpperCase());
        logger.info("Inquiry API === inquiryOrder Request ==> End");

        if (apiRequest.getCustomerName() == null || apiRequest.getCustomerName().isEmpty()) {
            return InquiryOrderAPIResponse.builder().orderService(orderServiceRepository.findAllCustomer()).build();
        }
        return InquiryOrderAPIResponse.builder().orderService(orderServiceRepository.findCustomerByCustomerName(apiRequest.getCustomerName().toUpperCase())).build();
    }

}
