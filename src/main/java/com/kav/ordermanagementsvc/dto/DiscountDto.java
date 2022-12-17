package com.kav.ordermanagementsvc.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiscountDto {
    private String id;
    private String orderId;
    private  String customerId;
    private double amount;
    private boolean claimed;
}
