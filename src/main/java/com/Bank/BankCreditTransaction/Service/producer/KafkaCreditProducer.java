package com.Bank.BankCreditTransaction.Service.producer;

import com.Bank.BankCreditTransaction.Models.Entities.CreditMessage;
import com.Bank.BankCreditTransaction.Models.Service.CreditResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaCreditProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCreditProducer.class);

    private final KafkaTemplate<String, CreditMessage> kafkaTemplate;

    public KafkaCreditProducer(@Qualifier("kafkaCreditTemplate") KafkaTemplate<String, CreditMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CreditMessage message) {
        LOGGER.info("Producing message {}", message);
        this.kafkaTemplate.send("TOPIC-CREDIT", message);
    }

}