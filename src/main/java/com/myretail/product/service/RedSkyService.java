package com.myretail.product.service;

import com.myretail.product.configuration.RedSkyProperties;
import com.myretail.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedSkyService {

  private final RestTemplate restTemplate;
  private final RedSkyProperties properties;

  @Retryable(value = RestClientException.class, maxAttempts = 2, backoff = @Backoff(delay = 500))
  public String getProductName(Long id) {
    try {
      String productDataString =
          restTemplate.getForObject(properties.getUrl(), String.class, properties.getKey(), id);
      JSONObject productDataJson = new JSONObject(productDataString);
      return productDataJson
          .getJSONObject("data")
          .getJSONObject("product")
          .getJSONObject("item")
          .getJSONObject("product_description")
          .getString("title");
    } catch (JSONException e) {
      log.error("Error while retrieving product details from redsky service.", e);
      throw new ProductNotFoundException(
          "Product details not found in Redsky for id: ".concat(String.valueOf(id)));
    }
  }
}
