package com.Bank.BankCreditTransaction.Service;

import com.Bank.BankCreditTransaction.Models.Service.Credit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CreditService {
    private static final Logger log = LoggerFactory.getLogger(CreditService.class);

    public Mono<Credit> FindCredit(String id){
        String url = "http://localhost:8081/api/Credit/"+id;
        Mono<Credit> oCreditMono = WebClient.create()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Credit.class);
        oCreditMono.subscribe(client -> log.info(client.toString()));
        return oCreditMono;
    }

    public Mono<Credit> UpdateCredit(Credit oCredit){
        String url = "http://localhost:8081/api/Credit/";
        Mono<Credit> oCreditMono = WebClient.create()
                .put()
                .uri(url)
                .body(Mono.just(oCredit), Credit.class)
                .retrieve()
                .bodyToMono(Credit.class);
        oCreditMono.subscribe(client -> log.info(client.toString()));
        return oCreditMono;
    }


}
