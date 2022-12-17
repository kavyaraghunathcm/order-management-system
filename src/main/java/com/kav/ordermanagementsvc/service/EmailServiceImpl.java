package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.constants.AppConstants;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmailServiceImpl implements EmailService {



    private final CustomerRepository customerRepository;

    public EmailServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void sendDiscountNotification() {

        List<Customer> goldCustomers= customerRepository.findByNoOfOrders(AppConstants.GOLD_THRESHOLD-1);
        for (Customer c:goldCustomers
             ) {
            sendEmail(c);
        }
        List<Customer> platinumCustomers= customerRepository.findByNoOfOrders(AppConstants.PLATINUM_THRESHOLD-1);
        for (Customer c:platinumCustomers
        ) {
            sendEmail(c);
        }

    }


    public void sendEmail(Customer c){
        System.out.println("Send email to customer:\n Hi "+c.getName()+", One more order to go! Your account with id "+c.getId()+" will be eligible for "+c.getLevel().name()+" membership. Get ready for exciting benefits on your next order.");
    }
}
