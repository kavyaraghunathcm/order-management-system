package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.dto.DiscountDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;

public interface DiscountService {

    DiscountDto claimDiscount(long discountId, long customerId, DiscountDto discountDto);

    void generateDiscount(long orderId);

    ListResponseDto<DiscountDto> findAll(Long customerId, int pageNo, int pageSize, String sortBy, String sortDir);

    DiscountDto findById(long customerId, long id);
}
