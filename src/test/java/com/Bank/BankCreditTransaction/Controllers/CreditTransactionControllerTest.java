package com.Bank.BankCreditTransaction.Controllers;

import com.Bank.BankCreditTransaction.Mock.CreditTransactionMock;
import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Entities.ResponseHandler;
import com.Bank.BankCreditTransaction.Service.CreditTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CreditTransactionController.class)
public class CreditTransactionControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CreditTransactionService creditTransactionService;

    @Test
    void findAllTest() {

        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(creditTransaction);

        Mockito
                .when(creditTransactionService.findAll())
                .thenReturn(Mono.just(responseHandler));

        webClient
                .get().uri("/api/CreditTransaction/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseHandler.class);
    }

    @Test
    void findByIdTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(creditTransaction);

        Mockito
                .when(creditTransactionService.find("630d920bf4c74f27ae62fcd4"))
                .thenReturn(Mono.just(responseHandler));

        webClient.get().uri("/api/CreditTransaction/{id}", "630d920bf4c74f27ae62fcd4")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseHandler.class);

        Mockito.verify(creditTransactionService, times(1)).find("630d920bf4c74f27ae62fcd4");
    }

    @Test
    void findByCreditTest() {
        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(creditTransaction);

        Mockito
                .when(creditTransactionService.findAllbyCredit("25210000000004"))
                .thenReturn(Mono.just(responseHandler));

        webClient.get().uri("/api/CreditTransaction/Credit/{id}", "25210000000004")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseHandler.class);

        Mockito.verify(creditTransactionService, times(1)).findAllbyCredit("25210000000004");
    }


    @Test
    void createPayTest() {

        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(creditTransaction);

        Mockito
                .when(creditTransactionService.RegisterCreditPay(creditTransaction)).thenReturn(Mono.just(responseHandler));

        webClient
                .post()
                .uri("/api/CreditTransaction/Pay/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(creditTransaction))
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void createChargeTest() {

        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionCharge();

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(creditTransaction);

        Mockito
                .when(creditTransactionService.RegisterCreditPay(creditTransaction)).thenReturn(Mono.just(responseHandler));

        webClient
                .post()
                .uri("/api/CreditTransaction/Charge/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(creditTransaction))
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void updateTest() {

        CreditTransaction creditTransaction = CreditTransactionMock.randomTransactionPay();

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(creditTransaction);

        Mockito
                .when(creditTransactionService.update("630d920bf4c74f27ae62fcd4",creditTransaction)).thenReturn(Mono.just(responseHandler));

        webClient
                .put()
                .uri("/api/CreditTransaction/{id}", "630d920bf4c74f27ae62fcd4")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(creditTransaction))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteTest() {

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.setMessage("Ok");
        responseHandler.setStatus(HttpStatus.OK);
        responseHandler.setData(null);

        Mockito
                .when(creditTransactionService.delete("630d920bf4c74f27ae62fcd4"))
                .thenReturn(Mono.just(responseHandler));

        webClient.delete().uri("/api/CreditTransaction/{id}", "630d920bf4c74f27ae62fcd4")
                .exchange()
                .expectStatus().isOk();
    }
    
}
