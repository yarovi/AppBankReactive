package org.yasmani.io.appbank.domain.port;

import org.yasmani.io.appbank.domain.model.Transfer;
import reactor.core.publisher.Mono;

public interface NotificationPort {
  Mono<Void> sendTransferNotification(Transfer transfer, boolean success);
  Mono<Void> sendBalanceAlert(String accountId, String message);
}