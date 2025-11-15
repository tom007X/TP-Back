package com.example.TruckMicroservice.config;

import com.example.TruckMicroservice.exception.ApiErrorResponse;
import com.example.TruckMicroservice.exception.BadRequestException;
import com.example.TruckMicroservice.exception.TruckDuplicateLicensePlate;


import com.example.TruckMicroservice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(),"Not Found", ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(),"Bad request", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleArgumentNotValid(MethodArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Error", ex.getMessage())
        );
    }

    @ExceptionHandler(TruckDuplicateLicensePlate.class)
    public ResponseEntity<ApiErrorResponse> handleLicensePlateDuplicate(TruckDuplicateLicensePlate ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(HttpStatus.CONFLICT.value(), "License Plate is not unique", ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage())
        );
    }



}
