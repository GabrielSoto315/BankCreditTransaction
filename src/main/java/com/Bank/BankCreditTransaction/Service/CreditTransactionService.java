package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Models.Service.Result;
import com.Bank.BankCreditTransaction.Repository.ICreditTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CreditTransactionService {

    @Autowired
    ICreditTransactionRepository oCreditTransactionRep;
    @Autowired
    CreditService oCrediSer;

    public Mono<Result> RegisterCreditPay(CreditTransaction oTransaction) {
        Result oResult = new Result();

        if (!oCrediSer.FindCredit(oTransaction.getId_credit_number()).blockOptional().isPresent()) {
            oResult.setMessage("CANCELED -- Credit not exists!!");
            return Mono.just(oResult);
        }

        return oCrediSer.FindCredit(oTransaction.getId_credit_number()).flatMap(x -> {
            BigDecimal newBalance = x.getBalance().add(oTransaction.getAmount());
            oTransaction.setTransaction_date(new Date());
            oTransaction.setType("PayCredit");
            oTransaction.setActive(true);
            oCreditTransactionRep.save(oTransaction).subscribe();
            x.setBalance(newBalance);
            oCrediSer.UpdateCredit(x);
            oResult.setMessage("CREDIT PAY!! new balance: "+ newBalance.toString());
            return Mono.just(oResult);
        });
    }

    public Mono<Result> RegisterCreditCharge(CreditTransaction oTransaction) {
        Result oResult = new Result();

        if (!oCrediSer.FindCredit(oTransaction.getId_credit_number()).blockOptional().isPresent()) {
            oResult.setMessage("CANCELED -- Credit not exists!!");
            return Mono.just(oResult);
        }

        return oCrediSer.FindCredit(oTransaction.getId_credit_number()).flatMap(x -> {
            BigDecimal newBalance = x.getBalance().add(oTransaction.getAmount().negate());
            if(newBalance.compareTo(BigDecimal.ZERO) < 0){
                oResult.setMessage("CANCELED!! not have enough");
                return Mono.just(oResult);
            }
            oTransaction.setTransaction_date(new Date());
            oTransaction.setType("Credit Charge");
            oTransaction.setActive(true);
            oCreditTransactionRep.save(oTransaction).subscribe();
            x.setBalance(newBalance);
            oCrediSer.UpdateCredit(x);
            oResult.setMessage("CREDIT CHARGED!! new balance: "+ newBalance.toString());
            return Mono.just(oResult);
        });
    }



}
