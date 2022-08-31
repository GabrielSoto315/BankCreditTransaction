package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import com.Bank.BankCreditTransaction.Models.Service.Credit;
import reactor.core.publisher.Mono;


public interface CreditService {

    Mono<CreditResponse> FindCredit(String id);
    Mono<CreditResponse> UpdateCredit(Credit oCredit);

}
