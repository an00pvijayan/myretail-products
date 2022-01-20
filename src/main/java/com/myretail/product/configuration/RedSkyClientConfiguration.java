package com.myretail.product.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RedSkyClientConfiguration {
  private final RedSkyProperties properties;

  @Bean
  public RestTemplate restTemplate() {

    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(properties.getConnectionTimeOut());
    factory.setReadTimeout(properties.getReadTimeOut());
    return new RestTemplate(factory);
  }
}
