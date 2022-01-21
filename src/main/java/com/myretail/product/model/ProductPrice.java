package com.myretail.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class representing product_price collection
 */
@Data
@Document(collection = "product_price")
public class ProductPrice {
  @Id private Long id;
  private Double value;
  private Currency currencyCode;
}
