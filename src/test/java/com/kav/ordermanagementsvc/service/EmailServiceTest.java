package com.kav.ordermanagementsvc.service;


import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.kav.ordermanagementsvc.constants.AppConstants.GOLD_THRESHOLD;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SuppressWarnings("ALL")
@SpringBootTest
public class EmailServiceTest {

    private CustomerRepository customerRepository;
    private EmailService emailService;

    @BeforeEach
    public void setup(){
        customerRepository= Mockito.mock(CustomerRepository.class);
        emailService= new EmailServiceImpl(customerRepository);
    }
    @Test
    public void givenCustomerList_whenCUstomerApproachesGoldOrPlatinum_thenSendEmail(){
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("A");
        customer1.setEmail("a@gmail.com");
        customer1.setNoOfOrders(GOLD_THRESHOLD-1);
        List<Customer> goldCustomers= new ArrayList<>();
        goldCustomers.add(customer1);
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("B");
        customer2.setEmail("b@gmail.com");
        customer2.setLevel(Level.GOLD);
        List<Customer> platinumCustomers= new ArrayList<>();
        platinumCustomers.add(customer2);


        when(customerRepository.findByNoOfOrders(anyInt())).thenReturn(goldCustomers,
                platinumCustomers);
        EmailService spy =spy(emailService);
        spy.sendDiscountNotification();

        verify(spy, times(2)).sendEmail(any(Customer.class));
    }
}
