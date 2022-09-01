package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Entities.EventMessage;
import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import com.Bank.BankCreditTransaction.Service.Implements.CreditServiceImp;
import com.Bank.BankCreditTransaction.Service.Implements.CreditTransactionServiceImp;
import com.Bank.BankCreditTransaction.Mock.CreditMock;
import com.Bank.BankCreditTransaction.Mock.CreditTransactionMock;
import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Repository.ICreditTransactionRepository;
import com.Bank.BankCreditTransaction.Service.producer.KafkaCreditProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    @InjectMocks
    private KafkaCreditProducer kafkaCreditProducer;
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

        StepVerifier.create(creditTransactionServiceImp.update(creditTransaction.getIdTransaction(), creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Done"))
                .expectComplete()
                .verify();
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

        StepVerifier.create(creditTransactionServiceImp.delete(creditTransaction.getIdTransaction()))
                .expectNextMatches(x -> x.getMessage().equals("Done"))
                .expectComplete()
                .verify();
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

    @Test
    void chargeNotFundsTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionCharge();
        Credit credit = CreditMock.randomCard();

        creditTransaction.setAmount(credit.getBalance().add(new BigDecimal(1000)));

        CreditResponse creditResponseMono = new CreditResponse();
        creditResponseMono.setMessage("Done");
        creditResponseMono.setStatus("OK");
        creditResponseMono.setData(credit);

        Mockito.when(creditTransactionRepository.save(creditTransaction))
                .thenReturn(Mono.just(creditTransaction));

        Mockito.when(CreditService.FindCredit("45210000000001"))
                .thenReturn(Mono.just(creditResponseMono));

        StepVerifier.create(creditTransactionServiceImp.RegisterCreditCharge(creditTransaction))
                .expectNextMatches(x -> x.getMessage().equals("Don't have enough funds"))
                .expectComplete()
                .verify();
    }
}
