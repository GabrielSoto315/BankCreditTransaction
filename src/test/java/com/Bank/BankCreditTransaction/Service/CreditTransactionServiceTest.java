package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Mock.CreditMock;
import com.Bank.BankCreditTransaction.Mock.CreditTransactionMock;
import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import com.Bank.BankCreditTransaction.Repository.ICreditTransactionRepository;
import com.Bank.BankCreditTransaction.Service.Implements.CreditServiceImp;
import com.Bank.BankCreditTransaction.Service.Implements.CreditTransactionServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;


@ExtendWith(SpringExtension.class)
public class CreditTransactionServiceTest {

    @InjectMocks
    private CreditServiceImp creditServiceImp;
    @InjectMocks
    private CreditTransactionServiceImp creditTransactionServiceImp;
    @Mock
    private ICreditTransactionRepository creditTransactionRepository;
    @Mock
    private CreditService CreditService;

    @Test
    void findAllTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.findAll())
                .thenReturn(Flux.just(creditTransaction));

        StepVerifier.create(creditTransactionServiceImp.findAll())
                .expectNextMatches(responseHandler -> responseHandler.getData() != null)
                .verifyComplete();
    }

    @Test
    void findByIdTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.existsById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(true));

        Mockito
                .when(creditTransactionRepository.findById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(creditTransaction));

        StepVerifier.create(creditTransactionServiceImp.find(creditTransaction.getIdTransaction()))
                .expectNextMatches(responseHandler -> responseHandler.getMessage().equals("Done"))
                .verifyComplete();
    }

    @Test
    void findByCreditTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.findAll())
                .thenReturn(Flux.just(creditTransaction));

        StepVerifier.create(creditTransactionServiceImp.findAllbyCredit(creditTransaction.getIdCredit()))
                .expectNextMatches(responseHandler -> responseHandler.getMessage().equals("Done"))
                .verifyComplete();
    }

    @Test
    void updateTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.existsById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(true));

        Mockito
                .when(creditTransactionRepository.findById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(creditTransaction));

        Mockito
                .when(creditTransactionRepository.save(creditTransaction))
                .thenReturn(Mono.just(creditTransaction));

        creditTransactionServiceImp.update(creditTransaction.getIdTransaction(), creditTransaction)
                .map(response -> StepVerifier.create(Mono.just(response))
                        .expectNextMatches(x -> x.getMessage().equals("Done"))
                        .expectComplete()
                        .verify());
    }

    @Test
    void updateNotFoundTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.existsById("630d920bf4c74f27ae62fcd4"))
                .thenReturn(Mono.just(false));

        StepVerifier.create(creditTransactionServiceImp.update(creditTransaction.getIdTransaction(), creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Not found"))
                .expectComplete()
                .verify();

    }

    @Test
    void deleteTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.existsById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(true));

        Mockito
                .when(creditTransactionRepository.findById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(creditTransaction));

        Mockito
                .when(creditTransactionRepository.save(creditTransaction))
                .thenReturn(Mono.just(creditTransaction));

        creditTransactionServiceImp.delete(creditTransaction.getIdTransaction())
                .map(response -> StepVerifier.create(Mono.just(response))
                        .expectNextMatches(x -> x.getMessage().equals("Done"))
                        .expectComplete()
                        .verify());
    }


    @Test
    void deleteNotFoundTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        Mockito
                .when(creditTransactionRepository.existsById(creditTransaction.getIdTransaction()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(creditTransactionServiceImp.delete(creditTransaction.getIdTransaction()))
                .expectNextMatches(x -> x.getMessage().equals("Not found"))
                .expectComplete()
                .verify();
    }

    @Test
    void payTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();
        Credit credit = CreditMock.randomAccount();

        CreditResponse creditResponseMono = new CreditResponse();
        creditResponseMono.setMessage("Done");
        creditResponseMono.setStatus("OK");
        creditResponseMono.setData(credit);

        Credit updateCredit = new Credit();
        updateCredit.setIdCredit("25210000000004");
        updateCredit.setBalance(credit.getBalance().add(creditTransaction.getAmount()));

        Mockito.when(creditTransactionRepository.save(creditTransaction))
                .thenReturn(Mono.just(creditTransaction));

        Mockito.when(CreditService.FindCredit("25210000000004"))
                .thenReturn(Mono.just(creditResponseMono));

        Mockito.when(CreditService.UpdateCredit(updateCredit))
                .thenReturn(Mono.just(creditResponseMono));

        StepVerifier.create(creditTransactionServiceImp.RegisterCreditPay(creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Done"))
                .expectComplete()
                .verify();
    }

    @Test
    void payNotFoundTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        CreditResponse creditResponseMono = new CreditResponse();
        creditResponseMono.setMessage("Not found");
        creditResponseMono.setStatus("NOT FOUND");
        creditResponseMono.setData(null);

        Mockito.when(CreditService.FindCredit("25210000000004"))
                .thenReturn(Mono.just(creditResponseMono));

        StepVerifier.create(creditTransactionServiceImp.RegisterCreditPay(creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Credit not found"))
                .expectComplete()
                .verify();
    }


    @Test
    void chargeTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionCharge();
        Credit credit = CreditMock.randomCard();

        CreditResponse creditResponseMono = new CreditResponse();
        creditResponseMono.setMessage("Done");
        creditResponseMono.setStatus("OK");
        creditResponseMono.setData(credit);

        Credit updateCredit = new Credit();
        updateCredit.setIdCredit("45210000000001");
        updateCredit.setBalance(credit.getBalance().add(creditTransaction.getAmount().negate()));

        Mockito.when(creditTransactionRepository.save(creditTransaction))
                .thenReturn(Mono.just(creditTransaction));

        Mockito.when(CreditService.FindCredit("45210000000001"))
                .thenReturn(Mono.just(creditResponseMono));

        Mockito.when(CreditService.UpdateCredit(updateCredit))
                .thenReturn(Mono.just(creditResponseMono));

        StepVerifier.create(creditTransactionServiceImp.RegisterCreditCharge(creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Done"))
                .expectComplete()
                .verify();
    }

    @Test
    void chargeNotFoundTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionCharge();

        CreditResponse creditResponseMono = new CreditResponse();
        creditResponseMono.setMessage("Not found");
        creditResponseMono.setStatus("NOT FOUND");
        creditResponseMono.setData(null);

        Mockito.when(CreditService.FindCredit(creditTransaction.getIdCredit()))
                .thenReturn(Mono.just(creditResponseMono));

        StepVerifier.create(creditTransactionServiceImp.RegisterCreditCharge(creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Credit not found"))
                .expectComplete()
                .verify();
    }

    @Test
    void chargeNotValidProductTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();
        Credit credit = CreditMock.randomAccount();

        CreditResponse creditResponseMono = new CreditResponse();
        creditResponseMono.setMessage("Done");
        creditResponseMono.setStatus("OK");
        creditResponseMono.setData(credit);

        Mockito.when(CreditService.FindCredit(creditTransaction.getIdCredit()))
                .thenReturn(Mono.just(creditResponseMono));

        StepVerifier.create(creditTransactionServiceImp.RegisterCreditCharge(creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Credit not valid for this transaction"))
                .expectComplete()
                .verify();
    }


/*


    @Override
    public Mono<ResponseHandler> RegisterCreditCharge(CreditTransaction oTransaction) {
        log.info("Start Credit card charge transaction");
        return creditService.FindCredit(oTransaction.getIdCredit()).flatMap(creditResponse -> {
            if (creditResponse.getData().equals(null)){
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
    */
}
