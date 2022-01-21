package com.myretail.product.repository;

import com.myretail.product.model.ProductPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for managing product_price table
 */
public interface ProductPriceRepository extends MongoRepository<ProductPrice, Long> {}
