package com.ms.orderservice.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
/**
 * Microservice Application for Domain Order Service
 *
 * @author Shreejit Panchal
 * @version 1.0
 * @date 20/03/2023
 */
@Slf4j
@SpringBootApplication
@EntityScan(basePackages = {"com.ms.orderservice"})
@ComponentScan(basePackages = {"com.solace.orderservice"})
public class MSOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MSOrderServiceApplication.class, args);
    }
}
