package com.Bank.BankCreditTransaction.Controllers;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Entities.ResponseHandler;
import com.Bank.BankCreditTransaction.Repository.ICreditTransactionRepository;
import com.Bank.BankCreditTransaction.Service.CreditTransactionService;
import com.Bank.BankCreditTransaction.Service.Implements.CreditTransactionServiceImp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/CreditTransaction/")
public class CreditTransactionController {

    @Autowired
    private CreditTransactionService creditTransactionService;


    private static final Logger log = LoggerFactory.getLogger(CreditTransactionController.class);

    /**
     * Lista todos las transacciones encontradas
     * @return
     */
    @GetMapping()
    public Mono<ResponseHandler> GetAll(){
        return creditTransactionService.findAll();
    }

    /**
     * Obtener resultado por id de transaccion
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseHandler> FindbyId(@PathVariable("id") String id){
        return creditTransactionService.find(id);
    }

    /**
     * Obtener resultado por id de credito
     * @param idCredit
     * @return
     */
    @GetMapping("/Credit/{idCredit}")
    public Mono<ResponseHandler> FindbyCredit(@PathVariable("idCredit") String idCredit){
        return creditTransactionService.findAllbyCredit(idCredit);
    }

    /**
     * Actualizar datos de cliente empresa
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseHandler> update(@PathVariable("id") String id, @RequestBody CreditTransaction creditTransaction) {
        return creditTransactionService.update(id, creditTransaction);
    }

    /**
     * Borrar datos por id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseHandler>  DeletebyId(@PathVariable("id") String id){
        return creditTransactionService.delete(id);
    }

    /**
     * Guardar transaccion de pago de credito de cuenta o tarjeta
     * @param oCreditTransaction
     * @return
     */
    @PostMapping("Pay/")
    public Mono<ResponseHandler> RegisterCreditPay(@RequestBody CreditTransaction oCreditTransaction){
        return creditTransactionService.RegisterCreditPay(oCreditTransaction);
    }

    /**
     * Guardar transaccion de cargo en tarjeta de credito
     * @param oCreditTransaction
     * @return
     */
    @PostMapping("Charge/")
    public Mono<ResponseHandler> SaveCreditCardCharge(@RequestBody CreditTransaction oCreditTransaction){
         return creditTransactionService.RegisterCreditCharge(oCreditTransaction);
    }
    
}