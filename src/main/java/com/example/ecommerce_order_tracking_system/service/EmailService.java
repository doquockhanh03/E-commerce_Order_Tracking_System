package com.example.ecommerce_order_tracking_system.service;

public interface EmailService {
    void sendMail(String to, String subject, String content);
}
