package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.dto.DiscountDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.entity.Discount;

import java.util.concurrent.Future;

public interface DiscountService {

    DiscountDto claimDiscount(long discountId, long customerId, DiscountDto discountDto);

    Future<Discount> generateDiscount(long orderId);

    ListResponseDto<DiscountDto> findAll(Long customerId, int pageNo, int pageSize, String sortBy, String sortDir);

    DiscountDto findById(long customerId, long id);
}
