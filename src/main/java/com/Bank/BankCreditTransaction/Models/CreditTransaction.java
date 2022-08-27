package com.Bank.BankCreditTransaction.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "CreditTransaction")
@Data
public class CreditTransaction {
    @Id
    private String id;
    private String number_credit;
    private Date transaction_date;
    private Number amount;
    private String type;
}
