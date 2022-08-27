package com.Bank.BankCreditTransaction.Repository;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICreditTransactionRepository extends ReactiveMongoRepository<CreditTransaction, String> {
}