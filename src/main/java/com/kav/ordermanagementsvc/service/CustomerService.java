package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.dto.CustomerDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;


public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto findById(long id);

    ListResponseDto<CustomerDto> findAll(int pageNo, int pageSize, String sortBy, String sortDir);

    CustomerDto updateCustomer(CustomerDto customerDto, Long id);

    void deleteCustomerById(long id);
}
