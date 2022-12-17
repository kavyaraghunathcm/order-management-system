package com.kav.ordermanagementsvc.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class DiscountAlreadyClaimedException extends RuntimeException {
    private final String fieldName;
    private final long fieldValue;


    public DiscountAlreadyClaimedException(String fieldName, long fieldValue) {
        super("Discount with "+fieldName+" "+fieldValue+" already claimed");
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
