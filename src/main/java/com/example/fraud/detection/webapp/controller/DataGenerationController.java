package com.example.fraud.detection.webapp.controller;

import com.example.fraud.detection.webapp.datasource.TransactionsGenerator;
import com.example.fraud.detection.webapp.service.KafkaTransactionsPusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class DataGenerationController {

    private boolean generatingTransactions = false;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private TransactionsGenerator transactionsGenerator;

    public DataGenerationController(KafkaTransactionsPusher kafkaTransactionsPusher) {
        transactionsGenerator = new TransactionsGenerator(kafkaTransactionsPusher, 1);
    }

    @GetMapping("/api/startTransactionsGeneration")
    public void startTransactionsGeneration() {
        log.info("start transactions generation.....");
        generateTransactions();
    }

    @GetMapping("/api/stopTransactionsGeneration")
    public void stopTransactionsGeneration() {
        transactionsGenerator.cancel();
        generatingTransactions = false;
    }

    private void generateTransactions() {
        if(!generatingTransactions) {
            executor.submit(transactionsGenerator);
            generatingTransactions = true;
        }
    }
}
