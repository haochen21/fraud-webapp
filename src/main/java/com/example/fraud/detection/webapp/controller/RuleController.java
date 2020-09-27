package com.example.fraud.detection.webapp.controller;

import com.example.fraud.detection.webapp.datasource.RulesBootstrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/rules")
public class RuleController {

   private RulesBootstrapper rulesBootstrapper;

    public RuleController(RulesBootstrapper rulesBootstrapper) {
        this.rulesBootstrapper = rulesBootstrapper;
    }

    @GetMapping("/pushToKafka")
    public void pushToKafka() {
        rulesBootstrapper.sendToKafka();
    }

}
