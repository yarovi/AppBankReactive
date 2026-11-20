package org.yasmani.io.appbank.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoConfigValidator {
  public MongoConfigValidator(
      @Value("${spring.mongodb.uri}") String uri) {

    System.out.println(
        "MongoDB configurado: " +
            (uri != null && !uri.isBlank())
    );
  }
}
