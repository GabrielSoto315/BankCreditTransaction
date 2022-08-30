package com.Bank.BankCreditTransaction.Mock;

import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Models.Service.Product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class CreditMock {

    public static Credit randomAccount(){
        Credit credit = new Credit();
        credit.setIdCredit("25210000000004");
        credit.setIdClient("1910000000003");
        credit.setAmount(BigDecimal.valueOf(42000));
        credit.setBalance(BigDecimal.valueOf(400));

        Product product = new Product();
        product.setName("Credit Account");
        product.setClientType("Person");

        credit.setProduct(product);

        return credit;
    }

    public static Credit randomCard(){
        Credit credit = new Credit();
        credit.setIdCredit("45210000000001");
        credit.setIdClient("1910000000005");
        credit.setAmount(BigDecimal.valueOf(9000));
        credit.setBalance(BigDecimal.valueOf(8000));

        Product product = new Product();
        product.setName("Credit Card");
        product.setClientType("Person");

        credit.setProduct(product);

        return credit;
    }
}
