package com.Bank.BankCreditTransaction.Controllers;

import com.Bank.BankCreditTransaction.Models.Documents.CreditTransaction;
import com.Bank.BankCreditTransaction.Models.Entities.CreditMessage;
import com.Bank.BankCreditTransaction.Models.Entities.ResponseHandler;
import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import com.Bank.BankCreditTransaction.Service.CreditTransactionService;
import com.Bank.BankCreditTransaction.Service.producer.KafkaCreditProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/CreditTransaction/")
public class CreditTransactionController {

    @Autowired
    private CreditTransactionService creditTransactionService;

    private final KafkaCreditProducer kafkaCreditProducer;

    @Autowired
    CreditTransactionController(KafkaCreditProducer kafkaStringProducer) {
        this.kafkaCreditProducer = kafkaStringProducer;
    }

    private static final Logger log = LoggerFactory.getLogger(CreditTransactionController.class);

    /**
     * Lista todos las transacciones encontradas
     * @return
     */
    @GetMapping()
    public Mono<ResponseHandler> getAll(){
        return creditTransactionService.findAll();
    }

    /**
     * Obtener resultado por id de transaccion
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseHandler> findbyId(@PathVariable("id") String id){
        return creditTransactionService.find(id);
    }

    /**
     * Obtener resultado por id de credito
     * @param idCredit
     * @return
     */
    @GetMapping("/Credit/{idCredit}")
    public Mono<ResponseHandler> findbyCredit(@PathVariable("idCredit") String idCredit){
        return creditTransactionService.findAllbyCredit(idCredit);
    }

    /**
     * Actualizar datos de transaccion
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
    public Mono<ResponseHandler>  deletebyId(@PathVariable("id") String id){
        return creditTransactionService.delete(id);
    }

    /**
     * Guardar transaccion de pago de credito de cuenta o tarjeta
     * @param oCreditTransaction
     * @return
     */
    @PostMapping("Pay/")
    public Mono<ResponseHandler> saveCreditPay(@RequestBody CreditTransaction oCreditTransaction){
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


    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic() {
        CreditMessage creditResponse = new CreditMessage();
        creditResponse.setIdCredit("Done");
        creditResponse.setBalance(new BigDecimal(2400));
        this.kafkaCreditProducer.sendMessage(creditResponse);
    }
    
}