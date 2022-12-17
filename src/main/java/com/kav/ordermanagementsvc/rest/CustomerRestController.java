package com.kav.ordermanagementsvc.rest;


import com.kav.ordermanagementsvc.constants.AppConstants;
import com.kav.ordermanagementsvc.dto.CustomerDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final CustomerService customerService;
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ListResponseDto> getAllCars(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                      @RequestParam(value="sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(customerService.findAll(pageNo,pageSize,sortBy,sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findCustomerById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }
    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
       log.debug("post request"+ customerDto.toString());
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto customerDto, @PathVariable long id) {
        return ResponseEntity.ok(customerService.updateCustomer(customerDto, id));

    }


}
