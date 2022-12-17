package com.kav.ordermanagementsvc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDto {
    private Long id;
    private Double payment;
    private boolean discountGenerated;
}
