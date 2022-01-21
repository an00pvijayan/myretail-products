package com.myretail.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.myretail.product.dto.ProductDto;
import com.myretail.product.dto.ProductPriceDto;
import com.myretail.product.exception.ProductNotFoundException;
import com.myretail.product.model.Currency;
import com.myretail.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProductService productService;

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  void testGetProductById() throws Exception {
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    ProductDto mockServiceResponse =
        new ProductDto(1L, "product 1", new ProductPriceDto(3.14, Currency.USD.name()));
    when(productService.getProductById(1L)).thenReturn(mockServiceResponse);
    MvcResult response =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/products/1").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals(
        mapper.writeValueAsString(mockServiceResponse),
        response.getResponse().getContentAsString());
  }

  @Test
  void testGetProductByIdInvalidProduct() throws Exception {

    when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException("No Product"));
    MvcResult response =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/products/1").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();
    assertEquals("No Product", response.getResponse().getContentAsString());
  }

  @Test
  void testUpdateProduct() throws Exception {
    ProductDto request =
        new ProductDto(1L, "product 1", new ProductPriceDto(3.14, Currency.USD.name()));
    MvcResult response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/products/1")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
            .andExpect(status().isNoContent())
            .andReturn();
    assertNotNull(response.getResponse().getContentAsString());
  }
}
