package com.demo.decisionengine.service;

import com.demo.decisionengine.constants.LoanDecision;
import com.demo.decisionengine.constants.UserCreditStatus;
import com.demo.decisionengine.dto.LoanDecisionDto;
import com.demo.decisionengine.dto.LoanInputDto;
import com.demo.decisionengine.dto.UserCreditProfileDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanDecisionService {

    private static final int MIN_LOAN_AMOUNT = 200000;
    private static final int MAX_LOAN_AMOUNT = 1000000;
    private static final int LOAN_AMOUNT_INCREMENT = 10000;
    private static final int MAX_LOAN_PERIOD_IN_MONTHS = 60;
    private static final int LOAN_PERIOD_INCREMENT = 1;

    private final UserCreditProfileComposerMockService userProfileComposerMock;

    public LoanDecisionService(UserCreditProfileComposerMockService userCreditProfileComposerMockService) {
        this.userProfileComposerMock = userCreditProfileComposerMockService;
    }

    public LoanDecisionDto getLoanDecision(LoanInputDto loanInputDto) {
        UserCreditProfileDto userCreditProfile = userProfileComposerMock.getUserCreditModifier(loanInputDto.getPersonalCode());

        if (userCreditProfile.getUserCreditStatus() == UserCreditStatus.ELIGIBLE) {
            double creditScore = calculateCreditScore(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(),
                    loanInputDto.getLoanPeriodInMonths());
            if (creditScore >= 1) {
                return calculateMaxApprovableAmount(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(),
                        loanInputDto.getLoanPeriodInMonths());
            } else {
                LoanDecisionDto decision = calculateMinApprovableAmount(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(), loanInputDto.getLoanPeriodInMonths());

                if (decision.getDecision() == LoanDecision.DECLINED) {
                    return adjustLoanPeriodForApproval(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(), loanInputDto.getLoanPeriodInMonths());
                }
                return decision;

            }
        } else {
            return new LoanDecisionDto(LoanDecision.DECLINED, loanInputDto.getLoanAmount(), loanInputDto.getLoanPeriodInMonths());
        }

    }

    private double calculateCreditScore(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        BigDecimal modifier = BigDecimal.valueOf(creditModifier);
        BigDecimal amount = BigDecimal.valueOf(loanAmount / 100);
        BigDecimal period = BigDecimal.valueOf(loanPeriodInMonths);

        BigDecimal score = modifier.divide(amount, 20, RoundingMode.HALF_UP).multiply(period);

        return score.doubleValue();
    }

    private LoanDecisionDto calculateMaxApprovableAmount(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        while (loanAmount + LOAN_AMOUNT_INCREMENT <= MAX_LOAN_AMOUNT) {
            loanAmount += LOAN_AMOUNT_INCREMENT;
            double currentScore = calculateCreditScore(creditModifier, loanAmount, loanPeriodInMonths);
            if (currentScore < 1) {
                loanAmount -= LOAN_AMOUNT_INCREMENT;
                break;
            }
        }
        return new LoanDecisionDto(LoanDecision.APPROVED, Math.min(loanAmount, MAX_LOAN_AMOUNT), loanPeriodInMonths);
    }

    private LoanDecisionDto calculateMinApprovableAmount(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        while (loanAmount - LOAN_AMOUNT_INCREMENT >= MIN_LOAN_AMOUNT) {
            loanAmount -= LOAN_AMOUNT_INCREMENT;
            double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriodInMonths);
            if (creditScore >= 1) {
                return new LoanDecisionDto(LoanDecision.APPROVED, loanAmount, loanPeriodInMonths);
            }
        }
        return new LoanDecisionDto(LoanDecision.DECLINED, loanAmount, loanPeriodInMonths);
    }

    private LoanDecisionDto adjustLoanPeriodForApproval(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        while (loanPeriodInMonths + LOAN_PERIOD_INCREMENT >= MAX_LOAN_PERIOD_IN_MONTHS) {
            loanPeriodInMonths += LOAN_PERIOD_INCREMENT;
            double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriodInMonths);
            if (creditScore >= 1) {
                return new LoanDecisionDto(LoanDecision.APPROVED, loanAmount, loanPeriodInMonths);
            }
        }
        return new LoanDecisionDto(LoanDecision.DECLINED, loanAmount, loanPeriodInMonths);
    }

}
