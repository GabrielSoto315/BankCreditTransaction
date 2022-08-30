package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface CreditService {

    Mono<CreditResponse> FindCredit(String id);
    Mono<CreditResponse> UpdateCredit(Credit oCredit);

}
