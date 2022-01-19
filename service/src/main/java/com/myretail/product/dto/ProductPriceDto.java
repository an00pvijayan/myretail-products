package com.myretail.product.dto;

import com.myretail.product.model.Currency;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class ProductPriceDto {
  @NotNull(message = "Price value is mandatory")
  private Double value;
  @NotNull(message = "Currency code is mandatory")
  private Currency currencyCode;
}
