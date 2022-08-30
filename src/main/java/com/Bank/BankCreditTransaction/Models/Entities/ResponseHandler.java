package com.Bank.BankCreditTransaction.Models.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHandler {

    private String message;
    private HttpStatus status;
    private Object data;
}
