package com.ms.orderservice.api.service.commonservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.orderservice.api.model.CreateOrderAPIRequest;
import com.ms.orderservice.api.model.EDAPublishOrderEventRequest;
import com.ms.orderservice.api.solace.HelperServices.PublishEventOnTopicHandler;
import com.solace.services.core.model.SolaceServiceCredentials;
import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * Atomic Service to Generate Event onto Solace Messaging PUBSUB+
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class PublishOrderEvent {
    private final PublishEventOnTopicHandler pubEventHandler = new PublishEventOnTopicHandler();
    Logger logger = LoggerFactory.getLogger(PublishOrderEvent.class);
    ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired
    private EDAPublishOrderEventRequest edaPublishOrderEventRequest;
    @Value("${eda.order.create.publish.topic}")
    private String topicName;
    @Autowired
    private SpringJCSMPFactory solaceFactory;
    @Autowired(required = false)
    private SpringJCSMPFactoryCloudFactory springJCSMPFactoryCloudFactory;
    @Autowired(required = false)
    private SolaceServiceCredentials solaceServiceCredentials;
    @Autowired(required = false)
    private JCSMPProperties jcsmpProperties;

    public boolean sendEvent(CreateOrderAPIRequest apiRequest, String orderId) {
        EDAPublishOrderEventRequest.order edaPublishOrderDtl = new EDAPublishOrderEventRequest.order();

        logger.info("SendEvent API === Request ==> Start");

        topicName += apiRequest.getOrderRequest().getProductType() + "/" + orderId; // Add Mobile Type/OrderId in Topic Hierarchy of the event
        Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
        logger.info("SendEvent Step 1 === Request - CustomerName : " + apiRequest.getOrderRequest().getCustomerName());

        edaPublishOrderEventRequest = EDAPublishOrderEventRequest.builder()
                    .order(edaPublishOrderDtl.builder()
                            .userId(apiRequest.getOrderRequest().getUserId())
                            .orderId(orderId)
                            .customerName(apiRequest.getOrderRequest().getCustomerName())
                            .productId(apiRequest.getOrderRequest().getProductId())
                            .productType(apiRequest.getOrderRequest().getProductType())
                            .productDesc(apiRequest.getOrderRequest().getProductDesc())
                            .quantity(apiRequest.getOrderRequest().getQuantity())
                            .orderDateTime(apiRequest.getOrderRequest().getOrderDateTime())
                            .build())
                    .transactionId(apiRequest.getTransactionId())
                    .correlationId(apiRequest.getTransactionId())
                    .build();

        try {
            String eventJsonString = jsonMapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(edaPublishOrderEventRequest);
            logger.info("SendEvent Step 2 === EDA Topic publish on : " + topicName + "\n jsonPayload : " + eventJsonString);

            final JCSMPSession session = solaceFactory.createSession();
            XMLMessageProducer pubEventObj = session.getMessageProducer(pubEventHandler);
            TextMessage jcsmpMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            jcsmpMsg.setText(eventJsonString);
            jcsmpMsg.setDeliveryMode(DeliveryMode.PERSISTENT);

            pubEventObj.send(jcsmpMsg, topic);
            logger.info("SendEvent Step 2.1 === EDA Publish Successful ==> End");
            return true;

        } catch (Exception e) {
            logger.info("Step-2-Err ===Error in SendEvent Error :" + e.getMessage());
            return false;
        }
    }
}
