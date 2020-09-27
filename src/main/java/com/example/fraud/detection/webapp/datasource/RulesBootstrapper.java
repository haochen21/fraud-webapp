package com.example.fraud.detection.webapp.datasource;

import com.example.fraud.detection.webapp.service.KafkaRulesPusher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RulesBootstrapper implements ApplicationRunner {

    private List<Rule> ruleList = new ArrayList<>();

    private KafkaRulesPusher kafkaRulesPusher;

    public RulesBootstrapper(KafkaRulesPusher kafkaRulesPusher) {
        this.kafkaRulesPusher = kafkaRulesPusher;
    }

    public void sendToKafka() {
        ruleList.forEach(rule -> kafkaRulesPusher.accept(rule));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Rule rule1 = new Rule();
        rule1.setRuleId(1);
        rule1.setRuleState(Rule.RuleState.ACTIVE);
        rule1.setGroupingKeyNames(Arrays.asList("payeeId","beneficiaryId"));
        rule1.setAggregateFieldName("paymentAmount");
        rule1.setAggregateFunctionType(Rule.AggregateFunctionType.SUM);
        rule1.setLimit(new BigDecimal(20000000));
        rule1.setLimitOperatorType(Rule.LimitOperatorType.GREATER);
        rule1.setWindowMinutes(43200);

        Rule rule2 = new Rule();
        rule2.setRuleId(2);
        rule2.setRuleState(Rule.RuleState.ACTIVE);
        rule2.setGroupingKeyNames(Arrays.asList("paymentType"));
        rule2.setAggregateFieldName("COUNT_FLINK");
        rule2.setAggregateFunctionType(Rule.AggregateFunctionType.SUM);
        rule2.setLimit(new BigDecimal(300));
        rule2.setLimitOperatorType(Rule.LimitOperatorType.LESS);
        rule2.setWindowMinutes(1440);


        Rule rule3 = new Rule();
        rule3.setRuleId(3);
        rule3.setRuleState(Rule.RuleState.ACTIVE);
        rule3.setGroupingKeyNames(Arrays.asList("beneficiaryId"));
        rule3.setAggregateFieldName("paymentAmount");
        rule3.setAggregateFunctionType(Rule.AggregateFunctionType.SUM);
        rule3.setLimit(new BigDecimal(10000000));
        rule3.setLimitOperatorType(Rule.LimitOperatorType.GREATER_EQUAL);
        rule3.setWindowMinutes(1440);

        Rule rule4 = new Rule();
        rule4.setRuleId(4);
        rule4.setRuleState(Rule.RuleState.ACTIVE);
        rule4.setGroupingKeyNames(Arrays.asList("paymentType"));
        rule4.setAggregateFieldName("COUNT_WITH_RESET_FLINK");
        rule4.setAggregateFunctionType(Rule.AggregateFunctionType.SUM);
        rule4.setLimit(new BigDecimal(100));
        rule4.setLimitOperatorType(Rule.LimitOperatorType.GREATER_EQUAL);
        rule4.setWindowMinutes(1440);

        ruleList.add(rule1);
        //ruleList.add(rule2);
        //ruleList.add(rule3);
        //ruleList.add(rule4);
    }
}
