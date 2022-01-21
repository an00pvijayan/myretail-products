package com.myretail.product.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class ProductPriceDto {
  @NotNull(message = "Price value is mandatory")
  private Double value;

  private String currencyCode;
}
