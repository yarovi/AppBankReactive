package org.yasmani.io.appbank.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yasmani.io.appbank.domain.model.Transfer;
import org.yasmani.io.appbank.domain.model.TransferRequest;
import org.yasmani.io.appbank.domain.model.TransferResponse;
import org.yasmani.io.appbank.domain.model.TransferStatus;
import org.yasmani.io.appbank.domain.port.AccountRepositoryPort;
import org.yasmani.io.appbank.domain.port.NotificationPort;
import org.yasmani.io.appbank.domain.port.TransferPort;
import org.yasmani.io.appbank.domain.port.TransferRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService implements TransferPort {
  private final AccountRepositoryPort accountRepository;
  private final TransferRepositoryPort transferRepository;
  private final NotificationPort notificationPort;

  @Override
  public Mono<TransferResponse> processTransfer(TransferRequest request) {
    return validateTransfer(request)
        .flatMap(validRequest -> executeTransfer(validRequest))
        .flatMap(transferResponse -> sendNotifications(transferResponse))
        .doOnSuccess(response -> log.info("Transferencia procesada exitosamente: {}", response.getTransactionId()))
        .doOnError(error -> log.error("Error procesando transferencia: {}", error.getMessage()));
  }

  @Override
  public Mono<Transfer> checkTransferStatus(String transactionId) {
    return transferRepository.findById(transactionId)
        .switchIfEmpty(Mono.error(new RuntimeException("Transferencia no encontrada")));
  }

  @Override
  public Flux<Transfer> getTransferReport(String accountId, String startDate, String endDate) {
    LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
    LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
    return transferRepository.findBySourceAccountIdAndCreatedAtBetween(accountId, start, end);
  }

  private Mono<TransferRequest> validateTransfer(TransferRequest request) {
    return Mono.just(request)
        .flatMap(req -> {
          if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a cero"));
          }
          return Mono.just(req);
        })
        .flatMap(req -> accountRepository.findById(req.getSourceAccountId())
            .switchIfEmpty(Mono.error(new RuntimeException("Cuenta origen no encontrada")))
            .flatMap(account -> {
              if (account.getBalance().compareTo(req.getAmount()) < 0) {
                return Mono.error(new RuntimeException("Saldo insuficiente en cuenta origen"));
              }
              return accountRepository.findById(req.getDestinationAccountId())
                  .switchIfEmpty(Mono.error(new RuntimeException("Cuenta destino no encontrada")))
                  .then(Mono.just(req));
            })
        );
  }

  private Mono<TransferResponse> executeTransfer(TransferRequest request) {
    return accountRepository.findById(request.getSourceAccountId())
        .flatMap(sourceAccount ->
            accountRepository.findById(request.getDestinationAccountId())
                .flatMap(destinationAccount -> {
                  // Actualizar saldos
                  BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(request.getAmount());
                  BigDecimal newDestinationBalance = destinationAccount.getBalance().add(request.getAmount());

                  sourceAccount.setBalance(newSourceBalance);
                  sourceAccount.setUpdatedAt(LocalDateTime.now());
                  destinationAccount.setBalance(newDestinationBalance);
                  destinationAccount.setUpdatedAt(LocalDateTime.now());

                  // Crear registro de transferencia
                  Transfer transfer = Transfer.builder()
                      .sourceAccountId(request.getSourceAccountId())
                      .destinationAccountId(request.getDestinationAccountId())
                      .amount(request.getAmount())
                      .description(request.getDescription())
                      .status(TransferStatus.COMPLETED)
                      .createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now())
                      .transactionCode(UUID.randomUUID().toString().substring(0, 8))
                      .build();

                  // Guardar todo en transacción reactiva
                  return accountRepository.save(sourceAccount)
                      .then(accountRepository.save(destinationAccount))
                      .then(transferRepository.save(transfer))
                      .map(savedTransfer -> TransferResponse.builder()
                          .transactionId(savedTransfer.getId())
                          .transactionCode(savedTransfer.getTransactionCode())
                          .status(savedTransfer.getStatus())
                          .message("Transferencia completada exitosamente")
                          .timestamp(LocalDateTime.now())
                          .sourceBalance(newSourceBalance)
                          .destinationBalance(newDestinationBalance)
                          .build());
                })
        );
  }

  private Mono<TransferResponse> sendNotifications(TransferResponse response) {
    return transferRepository.findById(response.getTransactionId())
        .flatMap(transfer -> {
          boolean success = response.getStatus() == TransferStatus.COMPLETED;
          return notificationPort.sendTransferNotification(transfer, success)
              .then(notificationPort.sendBalanceAlert(
                  transfer.getSourceAccountId(),
                  "Se ha debitado " + transfer.getAmount() + " de su cuenta"
              ))
              .thenReturn(response);
        });
  }
}
