package com.example.ecommerce_order_tracking_system.service.impl;

import com.example.ecommerce_order_tracking_system.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!mail")
@RequiredArgsConstructor
public class LogSenderServiceImpl implements EmailService {
    @Override
    public void sendMail(String to, String subject, String content) {
        log.info("[fake mail] to: {} | Subject: {}, Content: {}", to, subject, content);
    }
}
