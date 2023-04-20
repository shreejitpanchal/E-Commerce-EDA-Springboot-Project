/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ms.orderservice.api.solace.HelperServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.orderservice.api.service.updateorder.UpdateOrder;
import com.ms.orderservice.api.model.UpdateOrderRequest;
import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * Solace Consumer helper class
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Service
public class ConsumerEventOnUpdateTopicHandler implements XMLMessageListener {
    @Autowired
    private UpdateOrder updateOrder;
    @Autowired
    private UpdateOrderRequest updateOrderRequest;
    private static final Logger logger = LoggerFactory.getLogger(ConsumerEventOnUpdateTopicHandler.class);
    ObjectMapper jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final CountDownLatch latch = new CountDownLatch(1);

    public void onReceive(BytesXMLMessage msg) {
        if (msg instanceof BytesMessage) {
            logger.info("=== Received Byte Stream - ConsumEventOnUpdateTopicHandler TextMessage received: " + new String(((BytesMessage)msg).getData()));
            invokeUpdateCustomerMaster(new String(((BytesMessage)msg).getData()));
        }
        if (msg instanceof TextMessage) {
            logger.info("=== Received Text Stream - ConsumEventOnUpdateTopicHandler TextMessage received: " + ((TextMessage) msg).getText());
            invokeUpdateCustomerMaster(((TextMessage) msg).getText());
        } else {
            logger.info("=== Received un-castable Message .");
        }
        latch.countDown(); // unblock main thread
    }

    private void invokeUpdateCustomerMaster(String rawJson) {
        try {
            updateOrderRequest = jsonMapper.readValue(rawJson, UpdateOrderRequest.class);
        } catch (JsonProcessingException e) {
            logger.info("Error :::" + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("=== Print incoming OrderStatus : " + updateOrderRequest.getOrderStatus());
        updateOrder.updateOrder(updateOrderRequest);
    }

    public void onException(JCSMPException e) {
        logger.info("Consumer received exception:", e);
        latch.countDown(); // unblock main thread
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
