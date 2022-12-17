package com.kav.ordermanagementsvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
public class FailureDto {
    int errorCode;
    String message;
    String description;
    Date timestamp;

}
