package com.Bank.BankCreditTransaction.Service.Implements;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Entities.ResponseHandler;
import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Repository.ICreditTransactionRepository;
import com.Bank.BankCreditTransaction.Service.CreditService;
import com.Bank.BankCreditTransaction.Service.CreditTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CreditTransactionServiceImp implements CreditTransactionService {

    @Autowired
    private ICreditTransactionRepository creditTransactionRepository;
    @Autowired
    private CreditService creditService;

    private static final Logger log = LoggerFactory.getLogger(CreditTransactionServiceImp.class);


    @Override
    public Mono<ResponseHandler> findAll() {
        return creditTransactionRepository.findAll()
                .doOnNext(n -> log.info(n.toString()))
                .collectList()
                .map(x -> new ResponseHandler("Done", HttpStatus.OK, x))
                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @Override
    public Mono<ResponseHandler> find(String id) {
        return creditTransactionRepository.findById(id)
                .doOnNext(n -> log.info(n.toString()))
                .map(x -> new ResponseHandler("Done", HttpStatus.OK, x))
                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @Override
    public Mono<ResponseHandler> findAllbyCredit(String idCredit) {
        return creditTransactionRepository.findAll()
                .doOnNext(n -> log.info(n.toString()))
                .filter(f -> f.getIdCredit().equals(idCredit))
                .collectList()
                .map(x -> new ResponseHandler("Done", HttpStatus.OK, x))
                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @Override
    public Mono<ResponseHandler> update(String id, CreditTransaction creditTransaction) {
        return creditTransactionRepository.existsById(id).flatMap(check -> {
            if (check) {
                return creditTransactionRepository.findById(id)
                        .flatMap(x -> {
                            x.setIdClient(creditTransaction.getIdClient());
                            return creditTransactionRepository.save(x)
                                    .map(y -> new ResponseHandler("Done", HttpStatus.OK, y))
                                    .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
                        });
            }
            else
                return Mono.just(new ResponseHandler("Not found", HttpStatus.NOT_FOUND, null));
        });
    }

    @Override
    public Mono<ResponseHandler> delete(String id) {
        return creditTransactionRepository.existsById(id).flatMap(check ->{
            if (Boolean.TRUE.equals(check)){
                return creditTransactionRepository.findById(id).flatMap(x ->{
                    x.setActive(false);
                    return creditTransactionRepository.save(x)
                            .then(Mono.just(new ResponseHandler("Done", HttpStatus.OK, null)));
                });
            }
            else {
                return Mono.just(new ResponseHandler("Not found", HttpStatus.NOT_FOUND,null));
            }
        });
    }

    @Override
    public Mono<ResponseHandler> RegisterCreditPay(CreditTransaction creditTransaction) {

        log.info("Start credit pay transaction");
        return creditService.FindCredit(creditTransaction.getIdCredit()).flatMap(credit -> {
            if (credit.getData() == null){
                return Mono.just(new ResponseHandler("Credit not found", HttpStatus.NOT_FOUND, null));
            }else {
                creditTransaction.setIdClient(creditTransaction.getIdClient() == null? credit.getData().getIdClient() : creditTransaction.getIdClient());
                creditTransaction.setTransactionDate(new Date());}
                creditTransaction.setType("PayCredit");
                creditTransaction.setActive(true);
                creditTransaction.setOldBalance(credit.getData().getBalance());
                creditTransaction.setNewBalance(credit.getData().getBalance().add(creditTransaction.getAmount()));
                log.info("Transaction: " + creditTransaction);
                return creditTransactionRepository.save(creditTransaction).flatMap(trans ->{
                    Credit updateCredit = new Credit();
                    updateCredit.setIdCredit(trans.getIdCredit());
                    updateCredit.setBalance(trans.getNewBalance());
                    log.info("Credit changes: " + updateCredit);
                    return creditService.UpdateCredit(updateCredit).flatMap(up ->{
                       return Mono.just(new ResponseHandler("Done", HttpStatus.OK, up));
                    });
                });
            });
    }

    @Override
    public Mono<ResponseHandler> RegisterCreditCharge(CreditTransaction oTransaction) {
        log.info("Start Credit card charge transaction");
        return creditService.FindCredit(oTransaction.getIdCredit()).flatMap(creditResponse -> {
            if (creditResponse.getData() == null){
                return Mono.just(new ResponseHandler("Credit not found", HttpStatus.NOT_FOUND, null));
            }else {
                if (creditResponse.getData().getProduct().getName().equals("Credit Card")) {
                    if (creditResponse.getData().getBalance().add(oTransaction.getAmount().negate()).compareTo(BigDecimal.ZERO) < 0){
                        return Mono.just(new ResponseHandler("Don't have enough funds", HttpStatus.NOT_FOUND, null));
                    }else {
                        oTransaction.setIdClient(oTransaction.getIdClient() == null? creditResponse.getData().getIdClient() : oTransaction.getIdClient());
                        oTransaction.setTransactionDate(new Date());
                        oTransaction.setType("Credit Charge");
                        oTransaction.setActive(true);
                        oTransaction.setOldBalance(creditResponse.getData().getBalance());
                        oTransaction.setNewBalance(creditResponse.getData().getBalance().add(oTransaction.getAmount().negate()));
                        return creditTransactionRepository.save(oTransaction).flatMap(tran ->{
                            log.info("Transaction saved" + oTransaction.toString());
                            Credit updateCredit = new Credit();
                            updateCredit.setIdCredit(tran.getIdCredit());
                            updateCredit.setBalance(tran.getNewBalance());
                            return creditService.UpdateCredit(updateCredit).flatMap(up ->{
                                return Mono.just(new ResponseHandler("Done", HttpStatus.OK, up));
                            });
                        });
                    }
                }else {
                    return Mono.just(new ResponseHandler("Credit not valid for this transaction", HttpStatus.BAD_REQUEST, null));
                }
            }
        });
    }
}
