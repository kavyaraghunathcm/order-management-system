package com.kav.ordermanagementsvc.exception;


import com.kav.ordermanagementsvc.dto.FailureDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName= ((FieldError)error).getField();
            String message=error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        FailureDto failureDto= new FailureDto(HttpStatus.BAD_REQUEST.value(), errors.toString(),request.getDescription(false),new Date());
        return new ResponseEntity<>(failureDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FailureDto> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
        FailureDto failureDto= new FailureDto(HttpStatus.NOT_FOUND.value(),exception.getMessage(),webRequest.getDescription(false),new Date());
        return new ResponseEntity<>(failureDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<FailureDto> handleResourceAccessException(ResourceAccessException exception, WebRequest webRequest){
        FailureDto failureDto= new FailureDto(HttpStatus.UNAUTHORIZED.value(),exception.getMessage(),webRequest.getDescription(false),new Date());
        return new ResponseEntity<>(failureDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DiscountAlreadyClaimedException.class)
    public ResponseEntity<FailureDto> handleDiscountAlreadyClaimedException(DiscountAlreadyClaimedException exception, WebRequest webRequest){
        FailureDto failureDto= new FailureDto(HttpStatus.BAD_REQUEST.value(),exception.getMessage(),webRequest.getDescription(false),new Date());
        return new ResponseEntity<>(failureDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureDto> handleGlobalException(Exception exception, WebRequest webRequest){
        FailureDto failureDto= new FailureDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage(),webRequest.getDescription(false),new Date());
        return new ResponseEntity<>(failureDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
