package com.myretail.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class ProductsApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProductsApplication.class, args);
  }
}
