package org.yasmani.io.appbank.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.yasmani.io.appbank.domain.model.Transfer;
import org.yasmani.io.appbank.domain.port.TransferRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class MongoTransferAdapter implements TransferRepositoryPort {
  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Mono<Transfer> save(Transfer transfer) {
    return mongoTemplate.save(transfer);
  }

  @Override
  public Mono<Transfer> findById(String id) {
    return mongoTemplate.findById(id, Transfer.class);
  }

  @Override
  public Mono<Transfer> findByTransactionCode(String transactionCode) {
    Query query = new Query(Criteria.where("transactionCode").is(transactionCode));
    return mongoTemplate.findOne(query, Transfer.class);
  }

  @Override
  public Flux<Transfer> findBySourceAccountIdAndCreatedAtBetween(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
    Query query = new Query(Criteria.where("sourceAccountId").is(accountId)
        .and("createdAt").gte(startDate).lte(endDate));
    return mongoTemplate.find(query, Transfer.class);
  }
}
