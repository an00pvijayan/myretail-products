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

  /**
   * Method to retrieve Product by productId
   *
   * @param productId
   * @return product {@link ProductDto} product price is available in DB and in redsky service.
   * @throws ProductNotFoundException if product not found
   */
  public ProductDto getProductById(Long productId) {
    return buildProductFromId(productId);
  }

  /**
   * Updates product price value for valid productId
   *
   * @param productId
   * @param updatedProductDto
   * @throws ProductNotFoundException if productId is not found in DB
   */
  public void updateProduct(Long productId, ProductDto updatedProductDto) {
    ProductPrice productPrice =
        productPriceRepository
            .findById(productId)
            .orElseThrow(
                () -> {
                  log.error("Unable to update price for productId {}", productId);
                  throw new ProductNotFoundException(
                      "Product not found for price update. productId = "
                          .concat(String.valueOf(productId)));
                });
    productPrice.setValue(updatedProductDto.getCurrentPrice().getValue());
    productPriceRepository.save(productPrice);
  }

  /**
   * Method to retrieve product details from {@link RedSkyService} and DB, merges and create {@link
   * ProductDto}
   *
   * @param productId
   * @throws ProductNotFoundException if data was not retrieved from either RedSky or DB
   */
  private ProductDto buildProductFromId(Long productId) {
    CompletableFuture<Optional<ProductPrice>> productPriceCompletableFuture =
        CompletableFuture.supplyAsync(() -> productPriceRepository.findById(productId));
    CompletableFuture<String> productNameCompletableFuture =
        CompletableFuture.supplyAsync(() -> redSkyService.getProductName(productId));
    try {
      return productPriceCompletableFuture
          .thenCombine(
              productNameCompletableFuture,
              (productPriceOptional, productName) -> {
                if (productPriceOptional.isEmpty() || isBlank(productName)) {
                  throw new ProductNotFoundException(
                      "Product price details not found for productId: "
                          .concat(String.valueOf(productId)));
                }
                ProductPrice productPrice = productPriceOptional.get();
                return new ProductDto(
                    productPrice.getId(),
                    productName,
                    new ProductPriceDto(
                        productPrice.getValue(), productPrice.getCurrencyCode().name()));
              })
          .get();
    } catch (InterruptedException | ExecutionException e) {
      throw new ProductNotFoundException(e.getCause().getMessage());
    }
  }
}
