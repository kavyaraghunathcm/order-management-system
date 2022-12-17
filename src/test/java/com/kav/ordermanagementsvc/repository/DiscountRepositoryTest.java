package com.kav.ordermanagementsvc.repository;


import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dao.DiscountRepository;
import com.kav.ordermanagementsvc.dao.OrderRepository;
import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.entity.Discount;
import com.kav.ordermanagementsvc.entity.Order;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DiscountRepositoryTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    DiscountRepository discountRepository;

    public void givenDiscountObject_whenSave_thenReturnSavedDiscount() {
        Customer customer= new Customer();
        customer.setName("Mary James");
        customer.setEmail("maryjames@gmail.com");
        customer.setLevel(Level.GOLD);

        Customer savedCustomer= customerRepository.save(customer);

        Order order =new Order();
        order.setPayment(1000);
        order.setCustomer(savedCustomer);

        Order savedOrder= orderRepository.save(order);

        Discount discount =new Discount();
        discount.setAmount(customer.getLevel().discount* order.getPayment());

        Discount savedDiscount= discountRepository.save(discount);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0);
    }
}