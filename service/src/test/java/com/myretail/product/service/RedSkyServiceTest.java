package com.myretail.product.service;

import com.myretail.product.configuration.RedSkyProperties;
import com.myretail.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedSkyServiceTest {
  @Mock private RedSkyProperties properties;
  @Mock private RestTemplate restTemplate;
  @InjectMocks private RedSkyService redSkyService;

  @Test
  void testGetProductName() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    String responseString =
        new String(
            getClass()
                .getClassLoader()
                .getResourceAsStream("readsky_response.json")
                .readAllBytes());
    when(properties.getUrl()).thenReturn("http://mockredskyserver");
    when(properties.getKey()).thenReturn("mockKey");
    when(restTemplate.getForObject(properties.getUrl(), String.class, properties.getKey(), 1L))
        .thenReturn(responseString);
    Assertions.assertEquals("The Big Lebowski (Blu-ray)", redSkyService.getProductName(1L));
  }

  @Test
  public void testGetProductNameError() {
    when(properties.getUrl()).thenReturn("http://mockredskyserver");
    when(properties.getKey()).thenReturn("mockKey");
    when(restTemplate.getForObject(properties.getUrl(), String.class, properties.getKey(), 1L))
        .thenThrow(new RestClientException("Invalid Client"));
    assertThrows(
        ProductNotFoundException.class, () -> redSkyService.getProductName(1L), "Invalid Client");
  }
}
