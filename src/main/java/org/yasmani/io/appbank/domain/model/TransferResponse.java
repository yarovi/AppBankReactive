package org.yasmani.io.appbank.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {
  private String transactionId;
  private String transactionCode;
  private TransferStatus status;
  private String message;
  private LocalDateTime timestamp;
  private BigDecimal sourceBalance;
  private BigDecimal destinationBalance;
}
