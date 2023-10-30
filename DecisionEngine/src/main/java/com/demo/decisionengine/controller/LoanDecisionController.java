package com.demo.decisionengine.controller;

import com.demo.decisionengine.dto.LoanDecisionDto;
import com.demo.decisionengine.dto.LoanInputDto;
import com.demo.decisionengine.service.LoanDecisionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan/decision")
public class LoanDecisionController {

    private final LoanDecisionService loanDecisionService;

    public LoanDecisionController(LoanDecisionService loanDecisionService) {
        this.loanDecisionService = loanDecisionService;
    }

    @PostMapping
    public LoanDecisionDto getLoanDecision(@RequestBody @Valid LoanInputDto loanInputDto) {
        return loanDecisionService.getLoanDecision(loanInputDto);
    }
}
