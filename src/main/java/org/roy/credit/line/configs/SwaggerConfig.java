package org.roy.credit.line.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI creditLineOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Credit Line API")
                .description("Api to handle Credit Line Domain")
                .version("v0.0.1"));
  }
}
