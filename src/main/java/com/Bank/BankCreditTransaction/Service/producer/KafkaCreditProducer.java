package com.Bank.BankCreditTransaction.Service.producer;

import com.Bank.BankCreditTransaction.Models.Entities.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class KafkaCreditProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCreditProducer.class);

    private final KafkaTemplate<String, EventMessage> kafkaTemplate;

    public KafkaCreditProducer(@Qualifier("kafkaCreditTemplate") KafkaTemplate<String, EventMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(EventMessage event) {
        LOGGER.info("Producing message {}", event);
        this.kafkaTemplate.send("TOPIC-CREDIT", event);
    }

}