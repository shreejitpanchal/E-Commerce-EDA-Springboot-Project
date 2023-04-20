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

    public void sendEvent(CreateOrderAPIRequest apiRequest, String orderId) {
        logger.info("SendEvent API === Request ==> Start");

        topicName += apiRequest.getMobileType() + "/" + orderId; // Add Mobile Type/OrderId in Topic Hierarchy of the event
        Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
        logger.info("SendEvent Step 1 === Request - CustomerName : " + apiRequest.getCustomerName());

        edaPublishOrderEventRequest = EDAPublishOrderEventRequest.builder().orderId(orderId).customerName(apiRequest.getCustomerName()).mobileType(apiRequest.getMobileType()).orderDateTime(apiRequest.getOrderDateTime()).build();

        try {
            String eventJsonString = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(edaPublishOrderEventRequest);
            logger.info("SendEvent Step 2 === EDA Topic publish on : " + topicName + "\n jsonPayload : " + eventJsonString);

            final JCSMPSession session = solaceFactory.createSession();
            XMLMessageProducer pubEventObj = session.getMessageProducer(pubEventHandler);
            TextMessage jcsmpMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            jcsmpMsg.setText(eventJsonString);
            jcsmpMsg.setDeliveryMode(DeliveryMode.PERSISTENT);

            pubEventObj.send(jcsmpMsg, topic);
            logger.info("SendEvent Step 2.1 === EDA Publish Successful ==> End");

        } catch (Exception e) {
            logger.info("Step-2-Err ===Error in SendEvent Error :" + e.getMessage());
        }

    }

}
