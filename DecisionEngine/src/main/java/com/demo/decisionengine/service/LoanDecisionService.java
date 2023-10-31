package com.demo.decisionengine.service;

import com.demo.decisionengine.constants.LoanDecision;
import com.demo.decisionengine.constants.UserCreditStatus;
import com.demo.decisionengine.dto.LoanDecisionDto;
import com.demo.decisionengine.dto.LoanInputDto;
import com.demo.decisionengine.dto.UserCreditProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.demo.decisionengine.constants.LoanConstraintConstants.*;

@Service
public class LoanDecisionService {

    private static final Logger logger = LoggerFactory.getLogger(LoanDecisionService.class);
    private final UserCreditProfileComposerMockService userProfileComposerMock;

    public LoanDecisionService(UserCreditProfileComposerMockService userCreditProfileComposerMockService) {
        this.userProfileComposerMock = userCreditProfileComposerMockService;
    }

    public LoanDecisionDto getLoanDecision(LoanInputDto loanInputDto) {
        logger.info("Starting loan decision process for input: {}", loanInputDto);
        UserCreditProfileDto userCreditProfile = userProfileComposerMock.getUserCreditModifier(loanInputDto.getPersonalCode());

        if (userCreditProfile.getUserCreditStatus() == UserCreditStatus.ELIGIBLE) {
            double creditScore = calculateCreditScore(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(),
                    loanInputDto.getLoanPeriodInMonths());
            LoanDecisionDto decision;
            if (creditScore >= 1) {
                decision = calculateMaxApprovableAmountIfPossible(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(),
                        loanInputDto.getLoanPeriodInMonths());
                logger.info("Loan decision for personal code {}: {}", loanInputDto.getPersonalCode(), decision);
            } else {
                decision = calculateMinApprovableAmountIfPossible(userCreditProfile.getCreditModifier(), loanInputDto.getLoanAmount(), loanInputDto.getLoanPeriodInMonths());

                if (decision.getDecision() == LoanDecision.DECLINED) {
                    return adjustLoanPeriodForApprovalIfPossible(userCreditProfile.getCreditModifier(), MIN_LOAN_AMOUNT, loanInputDto.getLoanPeriodInMonths());
                }

            }
            logger.info("Loan decision for personal code {}: {}", loanInputDto.getPersonalCode(), decision);
            return decision;
        } else {
            LoanDecisionDto decision = new LoanDecisionDto(LoanDecision.DECLINED, loanInputDto.getLoanAmount(), loanInputDto.getLoanPeriodInMonths());
            logger.info("Loan decision for personal code {}: {}", loanInputDto.getPersonalCode(), decision);
            return decision;
        }

    }

    private double calculateCreditScore(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        BigDecimal modifier = BigDecimal.valueOf(creditModifier);
        BigDecimal amount = BigDecimal.valueOf(loanAmount / 100);
        BigDecimal period = BigDecimal.valueOf(loanPeriodInMonths);

        BigDecimal score = modifier.divide(amount, 20, RoundingMode.HALF_UP).multiply(period);

        return score.doubleValue();
    }

    private LoanDecisionDto calculateMaxApprovableAmountIfPossible(int creditModifier, int loanAmount, int loanPeriodInMonths) {
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

    private LoanDecisionDto calculateMinApprovableAmountIfPossible(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        while (loanAmount - LOAN_AMOUNT_INCREMENT >= MIN_LOAN_AMOUNT) {
            loanAmount -= LOAN_AMOUNT_INCREMENT;
            double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriodInMonths);
            if (creditScore >= 1) {
                return new LoanDecisionDto(LoanDecision.APPROVED, loanAmount, loanPeriodInMonths);
            }
        }
        return new LoanDecisionDto(LoanDecision.DECLINED, loanAmount, loanPeriodInMonths);
    }

    private LoanDecisionDto adjustLoanPeriodForApprovalIfPossible(int creditModifier, int loanAmount, int loanPeriodInMonths) {
        while (loanPeriodInMonths + LOAN_PERIOD_INCREMENT < MAX_LOAN_PERIOD_IN_MONTHS) {
            loanPeriodInMonths += LOAN_PERIOD_INCREMENT;
            double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriodInMonths);
            if (creditScore >= 1) {
                return new LoanDecisionDto(LoanDecision.APPROVED, loanAmount, loanPeriodInMonths);
            }
        }
        return new LoanDecisionDto(LoanDecision.DECLINED, loanAmount, loanPeriodInMonths);
    }

}
