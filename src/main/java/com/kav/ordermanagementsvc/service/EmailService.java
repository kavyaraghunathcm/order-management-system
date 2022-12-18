package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.entity.Customer;

public interface EmailService {
    void sendDiscountNotification();
    void sendEmail(Customer c);
}
