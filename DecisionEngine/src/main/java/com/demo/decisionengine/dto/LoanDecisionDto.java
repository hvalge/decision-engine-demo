package com.demo.decisionengine.dto;

import com.demo.decisionengine.constants.LoanDecision;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanDecisionDto {
    private LoanDecision decision;
    private Integer approvedAmount;
    private Integer approvedPeriod;

}
