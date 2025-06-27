package com.spl.triggerflow.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GraphQlConfig {

  @Bean
  public RouterFunction<ServerResponse> graphiQlRouterFunction() {
    RouterFunctions.Builder builder = RouterFunctions.route();
    builder = builder.GET("/graphiql",
        req -> ServerResponse.temporaryRedirect(URI.create("/graphiql/index.html")).build());
    return builder.build();
  }
}
