package com.Bank.BankCreditTransaction.Models.Entities;

import lombok.Data;

@Data
public class EventMessage {

    private String action;
    private Object data;
}
