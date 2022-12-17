package com.kav.ordermanagementsvc.rest;

import com.kav.ordermanagementsvc.constants.AppConstants;
import com.kav.ordermanagementsvc.dto.DiscountDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/customers/")
public class DiscountRestController {

    @Autowired
    DiscountService discountService;
    @GetMapping("/{customerId}/discounts")
    public ResponseEntity<ListResponseDto> getAllDiscounts(@PathVariable("customerId") Long customerId, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                      @RequestParam(value="sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(discountService.findAll(customerId,pageNo,pageSize,sortBy,sortDir));
    }


    @GetMapping("/{customerId}/discounts/{id}")
    public ResponseEntity<DiscountDto> findDiscountById(@PathVariable("customerId") long customerId, @PathVariable long id) {
        return ResponseEntity.ok(discountService.findById(customerId,id));
    }
    @PatchMapping("/{customerId}/discounts/{id}")
    public ResponseEntity<DiscountDto> claimDiscount(@PathVariable("customerId") long customerId, @PathVariable long id, @RequestBody DiscountDto discountDto) {
        return ResponseEntity.ok(discountService.claimDiscount(id,customerId,discountDto));

    }
}
