package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dao.OrderRepository;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.dto.OrderDto;
import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.entity.Order;
import com.kav.ordermanagementsvc.exception.ResourceAccessException;
import com.kav.ordermanagementsvc.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DiscountService discountService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(long customerId, OrderDto orderDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        Order order = modelMapper.map(orderDto, Order.class);
        order.setCustomer(customer);
        order = orderRepository.save(order);
        customer.setNoOfOrders(customer.getNoOfOrders()+1);
        customerRepository.save(customer);
        discountService.generateDiscount(order.getId());
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto findById(long customerId, long id ) {
        Order order =orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        if(order.getCustomer().getId()!=customerId){
            throw new ResourceAccessException("Customer","Order", "id",customerId,id);
        }
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public ListResponseDto<OrderDto> findAll(long customerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        Sort sort = sortDir.equals(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Page<Order> orderPage = orderRepository.findAllByCustomer(customer, PageRequest.of(pageNo, pageSize, sort));
        List<OrderDto> content = orderPage
                .getContent()
                .stream()
                .map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return new ListResponseDto<>(content, orderPage.getNumber(), orderPage.getSize(), orderPage.getTotalElements(), orderPage.getTotalPages(), orderPage.isLast());
    }

}
