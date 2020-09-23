package com.example.fraud.detection.webapp.datasource;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Slf4j
public class TransactionsGenerator implements Runnable {

    private Consumer<Transaction> consumer;

    private Integer maxRecordsPerSecond;

    private volatile boolean running = false;

    private Consumer<Transaction> decorated;

    private static double MIN_PAYMENT_AMOUNT = 5d;

    private static double MAX_PAYMENT_AMOUNT = 20;

    public TransactionsGenerator(Consumer<Transaction> consumer, Integer maxRecordsPerSecond) {
        this.maxRecordsPerSecond = maxRecordsPerSecond;
        this.consumer = consumer;
        init();
    }

    private void init() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(10))
                .limitForPeriod(maxRecordsPerSecond)
                .timeoutDuration(Duration.ofSeconds(20))
                .build();

        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        RateLimiter sendRateLimiter = rateLimiterRegistry
                .rateLimiter("sendRateLimiter");

        decorated = RateLimiter.decorateConsumer(sendRateLimiter, consumer);

        sendRateLimiter.getEventPublisher()
                .onSuccess(event -> log.info(event.toString()))
                .onFailure(event -> log.info(event.toString()));
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Transaction transaction = randomEvent();
                decorated.accept(transaction);
            } catch (Exception ex) {
                log.error("", ex);
            }
        }
    }

    public void cancel() {
        running = false;
        log.info("cancel transactions push........");
    }

    private Transaction randomEvent() {
        long transactionId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        double paymentAmountDouble = ThreadLocalRandom.current().nextDouble(MIN_PAYMENT_AMOUNT, MAX_PAYMENT_AMOUNT);
        paymentAmountDouble = Math.floor(paymentAmountDouble * 100) / 100;
        BigDecimal paymentAmount = BigDecimal.valueOf(paymentAmountDouble);

        return Transaction.builder()
                .transactionId(transactionId)
                .eventTime(System.currentTimeMillis())
                .payeeId(new Long(100))
                .beneficiaryId(new Long(20000))
                .paymentAmount(paymentAmount)
                .payMentType(paymentType(transactionId))
                .build();
    }

    private Transaction.PaymentType paymentType(long id) {
        int name = (int) (id % 2);
        switch (name) {
            case 0:
                return Transaction.PaymentType.ALIPAY;
            case 1:
                return Transaction.PaymentType.WEIXINPAY;
            default:
                throw new IllegalStateException("payment type error,id = " + id);
        }
    }
}
