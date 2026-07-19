package org.yasmani.io.appbank.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.yasmani.io.appbank.domain.model.Account;
import org.yasmani.io.appbank.domain.port.AccountRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MongoAccountAdapter implements AccountRepositoryPort {
  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Mono<Account> findById(String id) {
    return mongoTemplate.findById(id, Account.class);
  }

  @Override
  public Mono<Account> findByAccountNumber(String accountNumber) {
    Query query = new Query(Criteria.where("accountNumber").is(accountNumber));
    return mongoTemplate.findOne(query, Account.class);
  }

  @Override
  public Mono<Account> save(Account account) {
    return mongoTemplate.save(account);
  }

  @Override
  public Mono<Boolean> existsById(String id) {
    Query query = new Query(Criteria.where("id").is(id));
    return mongoTemplate.exists(query, Account.class);
  }

  @Override
  public Flux<Account> findAll() {
    return mongoTemplate.findAll(
Account.class
    );
  }
}
