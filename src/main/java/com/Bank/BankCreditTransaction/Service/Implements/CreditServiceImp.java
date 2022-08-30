package com.Bank.BankCreditTransaction.Service.Implements;

import com.Bank.BankCreditTransaction.Models.Service.Credit;
import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import com.Bank.BankCreditTransaction.Service.CreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImp implements CreditService {
    private static final Logger log = LoggerFactory.getLogger(CreditServiceImp.class);

    public Mono<CreditResponse> FindCredit(String id){
        String url = "http://localhost:18082/api/Credit/"+id;
        Mono<CreditResponse> oCreditMono = WebClient.create()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(CreditResponse.class);
        oCreditMono.subscribe(client -> log.info(client.toString()));
        return oCreditMono;
    }

    public Mono<CreditResponse> UpdateCredit(Credit oCredit){
        String url = "http://localhost:18082/api/Credit/"+oCredit.getIdCredit();
        Mono<CreditResponse> oCreditMono = WebClient.create()
                .put()
                .uri(url)
                .body(Mono.just(oCredit), Credit.class)
                .retrieve()
                .bodyToMono(CreditResponse.class);
        oCreditMono.subscribe(client -> log.info(client.toString()));
        return oCreditMono;
    }


}
