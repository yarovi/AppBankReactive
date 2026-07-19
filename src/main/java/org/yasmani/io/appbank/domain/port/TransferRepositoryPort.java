package org.yasmani.io.appbank.domain.port;

import org.yasmani.io.appbank.domain.model.Transfer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransferRepositoryPort {
  Mono<Transfer> save(Transfer transfer);
  Mono<Transfer> findById(String id);
  Mono<Transfer> findByTransactionCode(String transactionCode);
  Flux<Transfer> findBySourceAccountIdAndCreatedAtBetween(String accountId, LocalDateTime startDate, LocalDateTime endDate);
}
