package com.kav.ordermanagementsvc.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dao.DiscountRepository;
import com.kav.ordermanagementsvc.dao.OrderRepository;
import com.kav.ordermanagementsvc.dto.CustomerDto;
import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.service.CustomerService;
import org.assertj.core.internal.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerRestControllerITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DiscountRepository discountRepository;


    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp(){discountRepository.deleteAll();
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }


    //create customer
    @Test
    public void  givenCustomerDtoObject_whenCreateCustomer_thenReturnSavedCustomer () throws Exception{
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");

        ResultActions response= mockMvc.perform((post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto))));


        response.andDo(print())
                .andExpect(status().isCreated() )
                .andExpect(jsonPath("$.name"
                        ,is(customerDto.getName())))
                .andExpect(jsonPath("$.email"
                        ,is(customerDto.getEmail())))
                .andExpect(jsonPath("$.level",is(Level.REGULAR.name())));
    }

    @Test
    void givenListOfCustomers_whenGetAllCustomers_thenReturnCustomersList() throws Exception {
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        customerService.createCustomer(customerDto);

        CustomerDto customerDto1=new CustomerDto();
        customerDto1.setName("BCD");
        customerDto1.setEmail("bcd@gmail.com");
        customerService.createCustomer(customerDto1);

        ResultActions response= mockMvc.perform(get("/api/customers"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalElements",is(2)));

    }

    @Test
    void givenCustomerId_whengetCustomerById_thenReturnCustomerObject() throws Exception{
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);

        ResultActions response= mockMvc.perform(get("/api/customers/{id}",savedCustomerDto.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name"
                        ,is(customerDto.getName())))
                .andExpect(jsonPath("$.email"
                        ,is(customerDto.getEmail())))
                .andExpect(jsonPath("$.id",is(savedCustomerDto.getId().intValue())));
    }


    @Test
    public void givenInvalidCustomerId_whenGetCustomerId_thenReturnErrorDto() throws Exception{
        long customerId= 1L;
        ResultActions response= mockMvc.perform(get("/api/customers/{id}",customerId));

        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message",is("Customer not found with id : "+customerId)));
    }

    public void givenUpdatedCustomerDto_whenUpdateCustomer_thenReturnUpdatedCustomerDto() throws Exception{
        CustomerDto customerDto=new CustomerDto();
        customerDto.setName("ABC");
        customerDto.setEmail("abc@gmail.com");
        CustomerDto savedCustomerDto= customerService.createCustomer(customerDto);
        CustomerDto updatedCustomer= new CustomerDto();
        updatedCustomer.setName("Aaa bc");
        updatedCustomer.setEmail(customerDto.getEmail());
        ResultActions response= mockMvc.perform(put("/api/customers/{id}",savedCustomerDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name"
                        ,is(updatedCustomer.getName())))
                .andExpect(jsonPath("$.email"
                        ,is(updatedCustomer.getEmail())))
                .andExpect(jsonPath("$.id",is(savedCustomerDto.getId().intValue())));
    }

    @Test
    public void givenUpdatedCustomerWithInvalidId_whenUpdateEmployee_thenReturnErrorDto() throws Exception{
        long customerId=1L;
        CustomerDto updatedCustomer= new CustomerDto();
        updatedCustomer.setName("Aaa bc");
        updatedCustomer.setEmail("aa@gmail.com");

        ResultActions response= mockMvc.perform(put("/api/customers/{id}",customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)));


        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message",is("Customer not found with id : "+customerId)));
    }
}
