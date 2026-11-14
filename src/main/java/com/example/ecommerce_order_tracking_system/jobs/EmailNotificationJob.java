package com.example.ecommerce_order_tracking_system.jobs;

import com.example.ecommerce_order_tracking_system.common.enums.EmailStatus;
import com.example.ecommerce_order_tracking_system.entity.order.EmailNotifications;
import com.example.ecommerce_order_tracking_system.repository.order.EmailNotificationsRepo;
import com.example.ecommerce_order_tracking_system.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationJob {

    private final EmailNotificationsRepo emailNotificationsRepo;

    private final EmailService emailService;

    @Scheduled(fixedDelay = 10000)
    public void processPendingEmails(){

        List<EmailNotifications> emailNotifications = emailNotificationsRepo.findByStatusWithDetails(EmailStatus.PENDING);

        for(EmailNotifications emails : emailNotifications){
            try {
                emails.setStatus(EmailStatus.PROCESSING);
                emailNotificationsRepo.save(emails);
                emailService.sendMail(
                        emails.getUser().getEmail(),
                        "Cập nhật đơn hàng " + emails.getOrder().getTrackingNumber(),
                        "Đơn hàng của bạn đang trên đường giao!"
                );
                log.info("");
                emails.setStatus(EmailStatus.SENT);
            }catch (Exception e){
                emails.setStatus(EmailStatus.FAILED);
            }
            emailNotificationsRepo.save(emails);
        }

    }
}
