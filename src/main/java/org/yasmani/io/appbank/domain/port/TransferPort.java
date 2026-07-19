package org.yasmani.io.appbank.domain.port;

import org.yasmani.io.appbank.domain.model.Transfer;
import org.yasmani.io.appbank.domain.model.TransferRequest;
import org.yasmani.io.appbank.domain.model.TransferResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransferPort {
  Mono<TransferResponse> processTransfer(TransferRequest request);
  Mono<Transfer> checkTransferStatus(String transactionId);
  Flux<Transfer> getTransferReport(String accountId, String startDate, String endDate);
}
