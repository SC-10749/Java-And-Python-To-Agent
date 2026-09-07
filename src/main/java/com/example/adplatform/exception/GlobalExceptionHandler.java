package com.example.adplatform.exception;

import com.example.adplatform.common.ApiResponse;
import com.example.adplatform.common.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError())
                .getDefaultMessage();
        return ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException ex) {
        return ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
