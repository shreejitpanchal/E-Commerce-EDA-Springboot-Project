package com.ms.orderservice.api.solace;

import com.ms.orderservice.api.solace.HelperServices.ConsumerEventOnUpdateTopicHandler;
import com.solace.services.core.model.SolaceServiceCredentials;
import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/**
 * Solace EDA Subscriber for Update Customer Events
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class EventOrderUpdateTopicListner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(EventOrderUpdateTopicListner.class);
    @Value("${eda.order.update.subscribe.topic}")
    private String topicName;
    @Autowired
    private SpringJCSMPFactory solaceFactory;
    @Autowired(required=false) private SpringJCSMPFactoryCloudFactory springJCSMPFactoryCloudFactory;
    @Autowired(required=false) private SolaceServiceCredentials solaceServiceCredentials;
    @Autowired(required=false) private JCSMPProperties jcsmpProperties;

    @Autowired
    private ConsumerEventOnUpdateTopicHandler msgConsumer;
    @Override
    public void run(String... args) throws Exception {
        logger.info("==>Async EventOrderUpdate Topic Listner ==> Starting");

        final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
        final JCSMPSession session = solaceFactory.createSession();

        XMLMessageConsumer cons = session.getMessageConsumer(msgConsumer);

        session.addSubscription(topic);
        cons.start();
        logger.info("Connected to Solace... Awaiting Order update Event messages...");
        try{
            msgConsumer.getLatch().await(10, TimeUnit.SECONDS);

        }
        catch (Exception e)
        {
            logger.error("EventOrderUpdate Topic Lister Error :"+e.getMessage());
        }

    }
}
