package com.myretail.product.controller;

import com.myretail.product.dto.ProductDto;
import com.myretail.product.service.ProductService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/** Controller responsible for product endpoints */
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @Timed("get_product")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }
  @Timed("put_product")
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateProduct(
      @PathVariable("id") Long id, @Valid @RequestBody ProductDto productDto) {
    productService.updateProduct(id, productDto);
    return ResponseEntity.noContent().build();
  }
}
