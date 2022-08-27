package com.Bank.BankCreditTransaction.Controllers;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Service.Result;
import com.Bank.BankCreditTransaction.Repository.ICreditTransactionRepository;
import com.Bank.BankCreditTransaction.Service.CreditService;
import com.Bank.BankCreditTransaction.Service.CreditTransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/CreditTransaction/")
public class CreditTransactionController {

    @Autowired
    private ICreditTransactionRepository oCreditTransactionRep;
    @Autowired
    private CreditTransactionService oCreditTransactionSer;


    private static final Logger log = LoggerFactory.getLogger(CreditTransactionController.class);

    /**
     * Lista todos los resultados
     * @return
     */
    @GetMapping()
    public Flux<CreditTransaction> GetAll(){
        return Flux.fromIterable(oCreditTransactionRep.findAll().toIterable());
    }

    /**
     * Obtener resultado por id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mono<CreditTransaction>> FindbyId(@PathVariable("id") String id){
        Mono<CreditTransaction> oCredit = oCreditTransactionRep.findById(id);
        return new ResponseEntity<Mono<CreditTransaction>>(oCredit, oCredit != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Guardar nueva transaccion de credito
     * @param oCreditTransaction
     * @return
     */
    @PostMapping()
    public Mono<CreditTransaction> Save(@RequestBody CreditTransaction oCreditTransaction){
        oCreditTransactionRep.save(oCreditTransaction).subscribe();
        return Mono.just(oCreditTransaction);
    }

    /**
     * Actualizar datos de cliente empresa
     * @param oCreditTransaction
     * @return
     */
    @PutMapping()
    public Mono<CreditTransaction> Update(@RequestBody CreditTransaction oCreditTransaction){
        return oCreditTransactionRep.save(oCreditTransaction);
    }

    /**
     * Borrar datos por id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public void DeletebyId(@PathVariable("id") String id){
        oCreditTransactionRep.deleteById(id);
    }

    /**
     * Guardar transaccion de pago de credito de cuenta o tarjeta
     * @param oCreditTransaction
     * @return
     */
    @PostMapping("RegisterCreditPay/")
    public Mono<Result> RegisterCreditPay(@RequestBody CreditTransaction oCreditTransaction){
        return oCreditTransactionSer.RegisterCreditPay(oCreditTransaction);
    }

    /**
     * Guardar transaccion de cargo en tarjeta de credito
     * @param oCreditTransaction
     * @return
     */
    @PostMapping("RegisterCreditCharge/")
    public Mono<Result> SaveCreditCardCharge(@RequestBody CreditTransaction oCreditTransaction){
         return oCreditTransactionSer.RegisterCreditCharge(oCreditTransaction);
    }
    
}