package com.Bank.BankCreditTransaction.Models.Service;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class Credit {
    @Id
    private String idCredit;
    private String type;
    private BigDecimal amount;
    private BigDecimal balance;
    private String idClient;
    private Product product;

}
