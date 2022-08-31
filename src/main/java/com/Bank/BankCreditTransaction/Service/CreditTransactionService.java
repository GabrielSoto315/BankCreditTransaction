package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Entities.ResponseHandler;
import reactor.core.publisher.Mono;

public interface CreditTransactionService {

    Mono<ResponseHandler> findAll();
    Mono<ResponseHandler> find(String id);
    Mono<ResponseHandler> findAllbyCredit(String idCredit);
    Mono<ResponseHandler> update(String id, CreditTransaction creditTransaction);
    Mono<ResponseHandler> delete(String id);
    Mono<ResponseHandler> RegisterCreditPay(CreditTransaction creditTransaction);
    Mono<ResponseHandler> RegisterCreditCharge(CreditTransaction oTransaction);

}
