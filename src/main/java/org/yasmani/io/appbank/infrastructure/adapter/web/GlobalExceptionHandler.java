package org.yasmani.io.appbank.infrastructure.adapter.web;

import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    HttpStatus status;
    String message;

    if (ex instanceof IllegalArgumentException) {
      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    } else if (ex instanceof RuntimeException) {
      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "Error interno del servidor";
    }

    exchange.getResponse().setStatusCode(status);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    try {
      String errorJson = objectMapper.writeValueAsString(
          Map.of("timestamp", System.currentTimeMillis(), "status", status.value(), "error", message)
      );
      DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorJson.getBytes());
      return exchange.getResponse().writeWith(Mono.just(buffer));
    } catch (Exception e) {
      return Mono.error(e);
    }
  }
}
