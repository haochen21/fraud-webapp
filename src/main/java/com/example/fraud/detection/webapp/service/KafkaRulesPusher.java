package com.example.fraud.detection.webapp.service;

import com.example.fraud.detection.webapp.datasource.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class KafkaRulesPusher implements Consumer<Rule> {

    @Value("${kafka.topic.rules}")
    private String topic;

    private KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaRulesPusher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void accept(Rule rule) {
        log.info(rule.toString());
        kafkaTemplate.send(topic, rule);
    }
}
