package com.Bank.BankCreditTransaction.Models.Service;

import lombok.Data;

@Data
public class CreditResponse {
    private String message;
    private String status;
    private Credit data;
}
