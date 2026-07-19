package org.yasmani.io.appbank.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {
  @Id
  private String id;
  private String accountNumber;
  private String customerName;
  private BigDecimal balance;
  private String currency;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean active;
}
