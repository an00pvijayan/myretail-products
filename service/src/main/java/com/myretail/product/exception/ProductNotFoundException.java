package com.myretail.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductNotFoundException extends ResponseStatusException {
  public ProductNotFoundException(HttpStatus status, String reason) {
    super(status, reason);
  }

  public ProductNotFoundException(String reason) {
    super(HttpStatus.NOT_FOUND, reason);
  }
}
