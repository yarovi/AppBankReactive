package org.yasmani.io.appbank.infrastructure.adapter.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yasmani.io.appbank.domain.model.Transfer;
import org.yasmani.io.appbank.domain.model.TransferRequest;
import org.yasmani.io.appbank.domain.model.TransferResponse;
import org.yasmani.io.appbank.domain.port.TransferPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {
  private final TransferPort transferPort;

  @PostMapping("/process")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<TransferResponse> processTransfer(@Valid @RequestBody TransferRequest request) {
    return transferPort.processTransfer(request);
  }

  @GetMapping("/status/{transactionId}")
  public Mono<Transfer> checkStatus(@PathVariable String transactionId) {
    return transferPort.checkTransferStatus(transactionId);
  }

  @GetMapping("/report")
  public Flux<Transfer> getReport(
      @RequestParam String accountId,
      @RequestParam String startDate,
      @RequestParam String endDate) {
    return transferPort.getTransferReport(accountId, startDate, endDate);
  }
}
