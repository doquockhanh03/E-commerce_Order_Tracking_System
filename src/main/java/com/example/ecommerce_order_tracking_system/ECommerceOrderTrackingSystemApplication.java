package com.example.ecommerce_order_tracking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class
ECommerceOrderTrackingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceOrderTrackingSystemApplication.class, args);
    }

}
