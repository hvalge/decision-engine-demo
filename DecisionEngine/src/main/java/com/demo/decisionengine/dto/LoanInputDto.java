package com.demo.decisionengine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class LoanInputDto {

    @NotNull
    @Length(min = 1, max = 32)
    private String personalCode;

    @Min(value = 200000, message = "The minimum loan amount is 200000.")
    @Max(value = 1000000, message = "The maximum loan amount is 100000.")
    private int loanAmount;

    @Min(value = 12, message = "The minimum loan period is 12 months.")
    @Max(value = 60, message = "The maximum loan amount is 60 months.")
    private int loanPeriodInMonths;

}
