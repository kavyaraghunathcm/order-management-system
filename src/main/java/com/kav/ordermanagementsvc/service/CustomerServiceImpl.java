package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dto.CustomerDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        customerDto.setLevel(Level.REGULAR);
        return modelMapper.map(customerRepository.save(modelMapper.map(customerDto, Customer.class)), CustomerDto.class);
    }

    @Override
    public CustomerDto findById(long id) {
        return modelMapper.map(customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id)), CustomerDto.class);
    }

    @Override
    public ListResponseDto<CustomerDto> findAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
        List<CustomerDto> content = customerPage
                .getContent()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class)).collect(Collectors.toList());
        return new ListResponseDto<>(content, customerPage.getNumber(), customerPage.getSize(), customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto, Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customer.setEmail(customerDto.getEmail());
        customer.setName(customerDto.getName());
        return modelMapper.map(customerRepository.save(customer), CustomerDto.class);
    }

    @Override
    public void deleteCustomerById(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customerRepository.delete(customer);
    }
}
