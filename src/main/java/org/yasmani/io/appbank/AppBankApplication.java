package org.yasmani.io.appbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "org.yasmani.io.appbank.infrastructure.repository")
public class AppBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(AppBankApplication.class, args);
  }

}
