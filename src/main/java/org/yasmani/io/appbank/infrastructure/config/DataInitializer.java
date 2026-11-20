package org.yasmani.io.appbank.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.yasmani.io.appbank.domain.model.Account;
import org.yasmani.io.appbank.domain.port.AccountRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
  private final AccountRepositoryPort accountRepository;

  @Override
  public void run(String... args) {
    accountRepository.findAll()
        .collectList()
        .flatMap(accounts -> {
          if (accounts.isEmpty()) {
            return insertInitialData();
          }
          return Flux.empty().then();
        })
        .subscribe();
  }

  private Mono<Void> insertInitialData() {
    Account account1 = Account.builder()
        .accountNumber("ACC001")
        .customerName("Juan Pérez")
        .balance(BigDecimal.valueOf(10000.00))
        .currency("PEN")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .active(true)
        .build();

    Account account2 = Account.builder()
        .accountNumber("ACC002")
        .customerName("María García")
        .balance(BigDecimal.valueOf(5000.00))
        .currency("PEN")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .active(true)
        .build();

    return accountRepository.save(account1)
        .then(accountRepository.save(account2))
        .then();
  }
}