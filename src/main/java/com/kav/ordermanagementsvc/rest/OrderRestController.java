package com.kav.ordermanagementsvc.rest;


import com.kav.ordermanagementsvc.constants.AppConstants;
import com.kav.ordermanagementsvc.dto.OrderDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/api/customers")
public class OrderRestController {

    private final OrderService orderService;
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/{id}/orders")
    public ResponseEntity<ListResponseDto> getAllOrders(@PathVariable long id,
                                                        @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                      @RequestParam(value="sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(orderService.findAll(id, pageNo,pageSize,sortBy,sortDir));
    }

    @GetMapping("/{customerId}/orders/{id}")
    public ResponseEntity<OrderDto> findOrderById(@PathVariable("customerId") long customerId,@PathVariable long id) {
        return ResponseEntity.ok(orderService.findById(customerId,id));
    }
    @PostMapping("/{customerId}/orders")
    public ResponseEntity<OrderDto> addOrder(@PathVariable long customerId,@Valid @RequestBody OrderDto orderDto) {
       log.debug("post request"+ orderDto.toString());
        return new ResponseEntity<>(orderService.createOrder(customerId, orderDto), HttpStatus.CREATED);
    }
}
