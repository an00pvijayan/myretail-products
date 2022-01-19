package com.myretail.product.advice;

import com.myretail.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProductControllerAdvice {

  @ExceptionHandler(value = {ProductNotFoundException.class})
  public ResponseEntity<String> productNotFound(ProductNotFoundException pnf) {
    return ResponseEntity.status(pnf.getStatus()).body(pnf.getReason());
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<Map> productNotFound(MethodArgumentNotValidException validationException) {
    Map<String, String> errors = new HashMap<>();
    validationException
        .getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<String> serverError(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }
}
