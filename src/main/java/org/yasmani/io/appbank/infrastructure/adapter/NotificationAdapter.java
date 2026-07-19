package org.yasmani.io.appbank.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yasmani.io.appbank.domain.model.Transfer;
import org.yasmani.io.appbank.domain.port.NotificationPort;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NotificationAdapter implements NotificationPort {

  @Override
  public Mono<Void> sendTransferNotification(Transfer transfer, boolean success) {
    return Mono.fromRunnable(() -> {
      String message = success
          ? "Transferencia exitosa: " + transfer.getTransactionCode()
          : "Transferencia fallida: " + transfer.getTransactionCode();
      log.info("Enviando notificación: {}", message);
      // Aquí se implementaría el envío real de notificaciones
    });
  }

  @Override
  public Mono<Void> sendBalanceAlert(String accountId, String message) {
    return Mono.fromRunnable(() -> {
      log.info("Enviando alerta de saldo para cuenta {}: {}", accountId, message);
      // Aquí se implementaría el envío real de alertas
    });
  }
}