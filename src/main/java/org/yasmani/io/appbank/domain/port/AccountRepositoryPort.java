package org.yasmani.io.appbank.domain.port;

import org.yasmani.io.appbank.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepositoryPort {
  Mono<Account> findById(String id);
  Mono<Account> findByAccountNumber(String accountNumber);
  Mono<Account> save(Account account);
  Mono<Boolean> existsById(String id);

  Flux<Account> findAll();
}