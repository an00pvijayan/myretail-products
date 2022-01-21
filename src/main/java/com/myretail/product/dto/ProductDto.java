package com.myretail.product.dto;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
public class ProductDto {
  private Long id;

  private String name;

  @NotNull(message = "Current price is mandatory")
  @Valid
  private ProductPriceDto currentPrice;
}
