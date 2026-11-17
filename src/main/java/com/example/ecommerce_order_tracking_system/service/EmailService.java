package com.example.ecommerce_order_tracking_system.service;

import com.example.ecommerce_order_tracking_system.service.EmailService;

public interface EmailService {
    void sendMail(String to, String subject, String content);
}
