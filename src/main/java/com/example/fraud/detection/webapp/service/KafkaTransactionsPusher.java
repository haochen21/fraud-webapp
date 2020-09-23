package com.example.fraud.detection.webapp.service;

import com.example.fraud.detection.webapp.datasource.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class KafkaTransactionsPusher implements Consumer<Transaction> {

    @Value("${kafka.topic.transactions}")
    private String topic;

    private KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaTransactionsPusher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void accept(Transaction transaction) {
        log.info(transaction.toString());
        kafkaTemplate.send(topic, transaction);
    }
}
