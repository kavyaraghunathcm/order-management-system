package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.dto.OrderDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;

public interface OrderService {
    OrderDto createOrder(long customerId, OrderDto orderDto);

    OrderDto findById(long customerId, long id);

    ListResponseDto<OrderDto> findAll(long customerId, int pageNo, int pageSize, String sortBy, String sortDir);

}
