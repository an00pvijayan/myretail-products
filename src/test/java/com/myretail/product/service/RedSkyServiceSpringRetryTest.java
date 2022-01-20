package com.myretail.product.service;

import com.myretail.product.configuration.RedSkyProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@EnableRetry
@SpringBootTest(classes = {RedSkyService.class})
public class RedSkyServiceSpringRetryTest {
  @MockBean private RedSkyProperties properties;
  @MockBean private RestTemplate restTemplate;
  @Autowired private RedSkyService redSkyService;

  @Test
  public void testRedSkyRetry() {
    when(properties.getUrl()).thenReturn("http://mockredskyserver");
    when(properties.getKey()).thenReturn("mockKey");
    when(restTemplate.getForObject(properties.getUrl(), String.class, properties.getKey(), 1L))
        .thenThrow(new ResourceAccessException("Timeout"));
    assertThrows(ResourceAccessException.class, () -> redSkyService.getProductName(1L));
    verify(restTemplate, times(2))
        .getForObject(properties.getUrl(), String.class, properties.getKey(), 1L);
  }
}
