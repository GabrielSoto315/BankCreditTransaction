package com.Bank.BankCreditTransaction.Models.Entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditMessage {
    private String idCredit;
    private BigDecimal balance;
}
