package com.myretail.product.service;

import com.myretail.product.dto.ProductDto;
import com.myretail.product.dto.ProductPriceDto;
import com.myretail.product.exception.ProductNotFoundException;
import com.myretail.product.model.ProductPrice;
import com.myretail.product.repository.ProductPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.myretail.product.model.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock private ProductPriceRepository productPriceRepository;
  @Mock private RedSkyService redSkyService;
  @InjectMocks private ProductService productService;

  @Test
  void testGetProductById() {
    when(redSkyService.getProductName(1L)).thenReturn("My Product");
    ProductPrice mockProductPrice = new ProductPrice();
    mockProductPrice.setValue(3.14);
    mockProductPrice.setCurrencyCode(USD);
    mockProductPrice.setId(1L);
    when(productPriceRepository.findById(1L)).thenReturn(Optional.of(mockProductPrice));
    ProductDto product = productService.getProductById(1L);
    assertEquals("My Product", product.getName());
    assertEquals(USD.name(), product.getCurrentPrice().getCurrencyCode());
    assertEquals(3.14, product.getCurrentPrice().getValue());
    verify(redSkyService, times(1)).getProductName(1L);
    verify(productPriceRepository, times(1)).findById(1L);
  }

  @Test
  void testGetProductById_empty_response() {
    when(redSkyService.getProductName(1L)).thenReturn("");
    ProductPrice mockProductPrice = new ProductPrice();
    mockProductPrice.setValue(3.14);
    mockProductPrice.setCurrencyCode(USD);
    mockProductPrice.setId(1L);
    when(productPriceRepository.findById(1L)).thenReturn(Optional.of(mockProductPrice));
    ProductNotFoundException exception =
            assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    verify(redSkyService, times(1)).getProductName(1L);
    verify(productPriceRepository, times(1)).findById(1L);
  }

  @Test
  void testGetProductByIdErrorRedSky() {
    when(redSkyService.getProductName(1L)).thenThrow(new ProductNotFoundException("RedSky Error"));
    ProductPrice mockProductPrice = new ProductPrice();
    mockProductPrice.setValue(3.14);
    mockProductPrice.setCurrencyCode(USD);
    mockProductPrice.setId(1L);
    when(productPriceRepository.findById(1L)).thenReturn(Optional.of(mockProductPrice));
    ProductNotFoundException exception =
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    assertEquals("404 NOT_FOUND \"RedSky Error\"", exception.getReason());
    verify(redSkyService, times(1)).getProductName(1L);
    verify(productPriceRepository, times(1)).findById(1L);
  }

  @Test
  void testGetProductByIdInvalidProduct() {
    when(redSkyService.getProductName(1L)).thenReturn("My Product");
    when(productPriceRepository.findById(1L)).thenReturn(Optional.empty());
    ProductNotFoundException exception =
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    assertEquals(
        "404 NOT_FOUND \"Product price details not found for productId: 1\"", exception.getReason());
    verify(redSkyService, times(1)).getProductName(1L);
    verify(productPriceRepository, times(1)).findById(1L);
  }

  @Test
  void testUpdateProductPrice() {
    ArgumentCaptor<ProductPrice> productPriceArgumentCaptor =
        ArgumentCaptor.forClass(ProductPrice.class);
    ProductPrice mockProductPrice = new ProductPrice();
    mockProductPrice.setValue(3.14);
    mockProductPrice.setCurrencyCode(USD);
    mockProductPrice.setId(2L);
    when(productPriceRepository.findById(2L)).thenReturn(Optional.of(mockProductPrice));
    productService.updateProduct(
        2L, new ProductDto(2L, "EMPTY", new ProductPriceDto(5.0, USD.name())));
    verify(productPriceRepository, times(1)).save(productPriceArgumentCaptor.capture());
    assertEquals(5.0, productPriceArgumentCaptor.getValue().getValue());
  }

  @Test
  void testUpdateProductPriceNoProduct() {
    when(productPriceRepository.findById(2L)).thenReturn(Optional.empty());
    ProductNotFoundException exception =
        assertThrows(
            ProductNotFoundException.class,
            () ->
                productService.updateProduct(
                    2L, new ProductDto(2L, "EMPTY", new ProductPriceDto(5.0, USD.name()))));
    assertEquals("Product not found for price update. productId = 2", exception.getReason());
    verify(productPriceRepository, times(0)).save(any(ProductPrice.class));
  }
}
