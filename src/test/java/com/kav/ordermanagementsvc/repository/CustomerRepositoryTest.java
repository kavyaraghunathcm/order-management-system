package com.kav.ordermanagementsvc.repository;


import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.entity.Customer;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    //Test to save the customer.
    @Test
    public void givenCustomerObject_whenSave_thenReturnSavedCustomer(){
        Customer customer= new Customer();
        customer.setName("Mary James");
        customer.setEmail("maryjames@gmail.com");

        Customer savedCustomer= customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
        assertThat(savedCustomer.getLevel()).isEqualByComparingTo(Level.REGULAR);
    }

    public void givenCustomerLists_whenGetAll_thenReturnCustomerList(){
        Customer customer= new Customer();
        customer.setName("Mary James");
        customer.setEmail("maryjames@gmail.com");
        Customer customer1= new Customer();
        customer1.setName("Sam Thomas");
        customer1.setEmail("samthomas@gmail.com");

        Customer savedCustomer= customerRepository.save(customer);
        Customer savedCustomer1= customerRepository.save(customer1);
        List<Customer> customerList= customerRepository.findAll();

        assertThat(customerList).isNotNull();
        assertThat(customerList).hasSize(2);

    }

    }
