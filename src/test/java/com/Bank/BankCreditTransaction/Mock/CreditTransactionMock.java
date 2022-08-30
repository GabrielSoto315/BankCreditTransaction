package com.Bank.BankCreditTransaction.Mock;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class CreditTransactionMock {

    public static CreditTransaction randomTransactionPay(){

        CreditTransaction creditTransaction = new CreditTransaction();
        creditTransaction.setIdTransaction("630d920bf4c74f27ae62fcd4");
        creditTransaction.setIdCredit("25210000000004");
        creditTransaction.setIdClient("1910000000003");
        creditTransaction.setTransactionDate(new Date());
        creditTransaction.setType("PayCredit");
        creditTransaction.setActive(true);
        creditTransaction.setAmount(new BigDecimal(400));
        creditTransaction.setOldBalance(new BigDecimal(0));
        creditTransaction.setNewBalance(new BigDecimal(400));

        return creditTransaction;
    }
    public static CreditTransaction randomTransactionCharge(){

        CreditTransaction creditTransaction = new CreditTransaction();
        creditTransaction.setIdTransaction(UUID.randomUUID().toString());
        creditTransaction.setIdCredit("45210000000001");
        creditTransaction.setIdClient("1910000000005");
        creditTransaction.setTransactionDate(new Date());
        creditTransaction.setType("Credit Charge");
        creditTransaction.setActive(true);
        creditTransaction.setAmount(new BigDecimal(500));
        creditTransaction.setOldBalance(new BigDecimal(8000));
        creditTransaction.setNewBalance(new BigDecimal(7500));

        return creditTransaction;
    }
}
