package com.Bank.BankCreditTransaction.Models.Documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "CreditTransaction")
@Data
public class CreditTransaction {
    @Id
    private String idTransaction;
    private String idCredit;
    private Date transactionDate;
    private BigDecimal amount;
    private String type;
    private Boolean active;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;
    private String idClient;
}
