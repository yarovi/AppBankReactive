package org.yasmani.io.appbank.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
  private String sourceAccountId;
  private String destinationAccountId;
  private BigDecimal amount;
  private String description;
}
