package com.kav.ordermanagementsvc.config;


import com.kav.ordermanagementsvc.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronConfig {

    @Autowired
    EmailService emailService;
    @Scheduled(cron = "0 */5 * ? * *")
    public void discountAdvertisementEmail(){
        emailService.sendDiscountNotification();
    }
}
