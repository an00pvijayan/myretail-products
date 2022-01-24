package com.myretail.product.service;

import com.myretail.product.configuration.RedSkyProperties;
import com.myretail.product.exception.ProductNotFoundException;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedSkyService {

  private final RestTemplate restTemplate;
  private final RedSkyProperties properties;

  /**
   * Service to contact RedSky service and retrieve product details by tcin.
   * This service retries to retrieve information from redsky service in http status 5xx is returned from service.
   * @param id represents tcin
   * @return Product name if found in redsky service . Throws exception if not found.
   */
  @Timed("redsky_get-product")
  @Retryable(
      value = HttpServerErrorException.class,
      maxAttemptsExpression = "${redsky.client.retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "${redsky.client.retry.backoffMilliseconds}"))
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
