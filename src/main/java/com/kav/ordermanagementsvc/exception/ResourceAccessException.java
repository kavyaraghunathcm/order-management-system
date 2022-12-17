package com.kav.ordermanagementsvc.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ResourceAccessException extends RuntimeException {
    private final String parentResourceName;
    private final String childResourceName;

    private final String fieldName;
    private final long parentFieldValue;
    private final long childFieldValue;


    public ResourceAccessException(String parentResourceName, String childResourceName, String fieldName, long parentFieldValue, long childFieldValue) {
        super(childResourceName + " of " + fieldName+ " "+childFieldValue+" does not belong to "+ parentResourceName+ " of "+ fieldName+" "+ parentFieldValue);

        this.parentResourceName = parentResourceName;
        this.childResourceName = childResourceName;
        this.fieldName = fieldName;
        this.parentFieldValue = parentFieldValue;
        this.childFieldValue = childFieldValue;
    }

}
