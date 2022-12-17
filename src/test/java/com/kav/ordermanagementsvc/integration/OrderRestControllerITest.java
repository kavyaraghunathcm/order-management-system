  package com.kav.ordermanagementsvc.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dao.DiscountRepository;
import com.kav.ordermanagementsvc.dao.OrderRepository;
import com.kav.ordermanagementsvc.dto.CustomerDto;
import com.kav.ordermanagementsvc.dto.OrderDto;
import com.kav.ordermanagementsvc.service.CustomerService;
import com.kav.ordermanagementsvc.service.OrderService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    OrderService orderService;

    @BeforeEach
    void setUp(){
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }


    @AfterEach
     void cleanUp(){
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }


    //create customer
    @Test
    public void  givenOrderDtoObject_whenCreateOrder_thenReturnSavedOrder () throws Exception{
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);
        OrderDto orderDto= new OrderDto();
        orderDto.setPayment(1000.0);

        ResultActions response= mockMvc.perform((post("/api/customers/{customerId}/orders",savedCustomerDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto))));


        response.andDo(print())
                .andExpect(status().isCreated() )
                .andExpect(jsonPath("$.payment"
                        ,is(orderDto.getPayment())));
    }

    @Test
    void givenListOfOrders_whenGetAllOrders_thenReturnOrdersList() throws Exception {
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);

        OrderDto orderDto= new OrderDto();
        orderDto.setPayment(1000.0);
        orderService.createOrder(savedCustomerDto.getId(),orderDto);
        OrderDto orderDto1= new OrderDto();
        orderDto1.setPayment(2000.0);
        orderService.createOrder(savedCustomerDto.getId(),orderDto1);

        ResultActions response= mockMvc.perform(get("/api/customers/{customerId}/orders",savedCustomerDto.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalElements",is(2)));

    }

    @Test
    void givenOrderId_whengetOrderById_thenReturnCustomerObject() throws Exception{
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);

        OrderDto orderDto= new OrderDto();
        orderDto.setPayment(1000.0);
        OrderDto saveOrderDto= orderService.createOrder(savedCustomerDto.getId(),orderDto);

        ResultActions response= mockMvc.perform(get("/api/customers/{customerId}/orders/{id}",savedCustomerDto.getId(),saveOrderDto.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.payment"
                        ,is(saveOrderDto.getPayment())))
                .andExpect(jsonPath("$.id",is(saveOrderDto.getId().intValue())));
    }

    @Test
    void givenOrderId_whenGetOrderByIdWithInvalidCustomerId_thenReturnErrorDto() throws Exception{
        Long customerId= 0L;
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);

        OrderDto orderDto= new OrderDto();
        orderDto.setPayment(1000.0);
        OrderDto saveOrderDto= orderService.createOrder(savedCustomerDto.getId(),orderDto);

        ResultActions response= mockMvc.perform(get("/api/customers/{customerId}/orders/{id}",customerId,saveOrderDto.getId()));

        response.andExpect(status().is4xxClientError())
                .andDo(print());
    }
    @Test
    void givenInvalidOrderId_whenGetOrderById_thenReturnErrorDto() throws Exception{

        Long orderId= 1L;
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);

        ResultActions response= mockMvc.perform(get("/api/customers/{customerId}/orders/{id}",savedCustomerDto.getId(),orderId));

        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message",is("Order not found with id : "+orderId)));;
    }

}
