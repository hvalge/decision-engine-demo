package com.demo.decisionengine.controller;

import com.demo.decisionengine.service.LoanDecisionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loan/decision")
public class LoanDecisionController {

    private final LoanDecisionService loanDecisionService;

    public LoanDecisionController(LoanDecisionService loanDecisionService) {
        this.loanDecisionService = loanDecisionService;
    }

    @GetMapping
    public Object getLoanDecision() {
        return "123";
    }
}
