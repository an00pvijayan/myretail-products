package com.myretail.product.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "redsky.client")
public class RedSkyProperties {
  private String url;
  private String key;
  private int connectionTimeOut;
  private int readTimeOut;
}
