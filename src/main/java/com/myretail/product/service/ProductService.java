package com.myretail.product.service;

import com.myretail.product.dto.ProductDto;
import com.myretail.product.dto.ProductPriceDto;
import com.myretail.product.exception.ProductNotFoundException;
import com.myretail.product.model.ProductPrice;
import com.myretail.product.repository.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductPriceRepository productPriceRepository;
  private final RedSkyService redSkyService;

  public ProductDto getProductById(Long id) {
    return buildProductFromId(id);
  }

  public void updateProduct(Long id, ProductDto updatedProductDto) {
    ProductPrice productPrice =
        productPriceRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Unable to update price for id {}", id);
                  throw new ProductNotFoundException(
                      "Product not found for price update. id = ".concat(String.valueOf(id)));
                });
    productPrice.setValue(updatedProductDto.getCurrentPrice().getValue());
    productPriceRepository.save(productPrice);
  }

  private ProductDto buildProductFromId(Long id) {
    CompletableFuture<Optional<ProductPrice>> productPriceCompletableFuture =
        CompletableFuture.supplyAsync(() -> productPriceRepository.findById(id));
    CompletableFuture<String> productNameCompletableFuture =
        CompletableFuture.supplyAsync(() -> redSkyService.getProductName(id));
    try {
      return productPriceCompletableFuture
          .thenCombine(
              productNameCompletableFuture,
              (productPriceOptional, productName) -> {
                if (productPriceOptional.isEmpty() || isBlank(productName)) {
                  throw new ProductNotFoundException(
                      "Product price details not found for id: ".concat(String.valueOf(id)));
                }
                ProductPrice productPrice = productPriceOptional.get();
                return new ProductDto(
                    productPrice.getId(),
                    productName,
                    new ProductPriceDto(productPrice.getValue(), productPrice.getCurrencyCode()));
              })
          .get();
    } catch (InterruptedException | ExecutionException e) {
      throw new ProductNotFoundException(e.getCause().getMessage());
    }
  }
}
