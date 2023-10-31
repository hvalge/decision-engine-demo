package com.demo.decisionengine.service;

import com.demo.decisionengine.constants.LoanDecision;
import com.demo.decisionengine.dto.LoanDecisionDto;
import com.demo.decisionengine.dto.LoanInputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.demo.decisionengine.constants.LoanConstraintConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoanDecisionServiceTests {

    private LoanDecisionService service;

    private String personalCodeOfUserInDebt;
    private String personalCodeOfUserSegment1;
    private String personalCodeOfUserSegment3;


    @BeforeEach
    void setUp() {
        // Service mocking is skipped, as service dependency is already a mock itself.
        UserCreditProfileComposerMockService mockService = new UserCreditProfileComposerMockService();
        service = new LoanDecisionService(mockService);
        personalCodeOfUserInDebt = "49002010965";
        personalCodeOfUserSegment1 = "49002010976";
        personalCodeOfUserSegment3 = "49002010998";
    }

    @Test
    void testLoanDecisionForUserWithDebt() {
        LoanDecisionDto decision = service.getLoanDecision(new LoanInputDto(personalCodeOfUserInDebt, 500000, 24));
        assertEquals(LoanDecision.DECLINED, decision.getDecision());
    }

    @Test
    void testLoanDecisionForNonExistentUser() {
        String noOnesPersonalCode = "11111111111";
        assertThrows(IllegalArgumentException.class, () -> {
            service.getLoanDecision(new LoanInputDto(noOnesPersonalCode, 500000, 24));
        });
    }

    @Test
    void testLoanDecisionAdjustsLoanAmountAndGetsPositiveCreditScore() {
        LoanDecisionDto decision = service.getLoanDecision(new LoanInputDto(personalCodeOfUserSegment1, MAX_LOAN_AMOUNT, MIN_LOAN_PERIOD_IN_MONTHS));
        assertEquals(LoanDecision.APPROVED, decision.getDecision());
        assertTrue(decision.getApprovedAmount() < MAX_LOAN_AMOUNT);
    }

    @Test
    void testLoanDecisionAdjustsLoanPeriodAndGetsPositiveCreditScore() {
        LoanDecisionDto decision = service.getLoanDecision(new LoanInputDto(personalCodeOfUserSegment1, MIN_LOAN_AMOUNT, MIN_LOAN_PERIOD_IN_MONTHS));
        assertEquals(LoanDecision.APPROVED, decision.getDecision());
        assertTrue(decision.getApprovedPeriod() > 12);
    }

    @Test
    void testLoanDecisionDoesNotGoBelowMinimumLoanAmount() {
        LoanDecisionDto decision = service.getLoanDecision(new LoanInputDto(personalCodeOfUserSegment1, 300000, MIN_LOAN_PERIOD_IN_MONTHS));
        assertEquals(LoanDecision.APPROVED, decision.getDecision());
        assertEquals(MIN_LOAN_AMOUNT, decision.getApprovedAmount());
    }

    @Test
    void testLoanDecisionDoesNotExceedMaximumLoanAmount() {
        LoanDecisionDto decision = service.getLoanDecision(new LoanInputDto(personalCodeOfUserSegment3, 900000, 30));
        assertEquals(LoanDecision.APPROVED, decision.getDecision());
        assertEquals(MAX_LOAN_AMOUNT, decision.getApprovedAmount());
    }

    @Test
    void testCreditScoreIsCalculatedAsExactlyOne() {
        int amountJustEnough = 450000;
        int loanPeriodJustEnough = 45;

        LoanDecisionDto decisionJustEnough = service.getLoanDecision(new LoanInputDto(personalCodeOfUserSegment1, amountJustEnough, loanPeriodJustEnough));
        assertEquals(LoanDecision.APPROVED, decisionJustEnough.getDecision());
    }
}
