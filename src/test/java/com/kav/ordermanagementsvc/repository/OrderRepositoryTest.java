package com.kav.ordermanagementsvc.repository;


import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dao.OrderRepository;
import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.entity.Order;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Test
    public void givenOrderObject_whenSave_thenReturnSavedOrder(){
        Customer customer= new Customer();
        customer.setName("Mary James");
        customer.setEmail("maryjames@gmail.com");

        Customer savedCustomer= customerRepository.save(customer);

        Order order =new Order();
        order.setPayment(1000);
        order.setCustomer(savedCustomer);

        Order savedOrder= orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0);
    }
}
