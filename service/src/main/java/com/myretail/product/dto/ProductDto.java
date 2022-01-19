package com.myretail.product.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class ProductDto {
  @NotNull(message = "Product id is mandatory")
  private Long id;
  private String name;
  @NotNull(message = "Current price is mandatory")
  private ProductPriceDto currentPrice;
}
