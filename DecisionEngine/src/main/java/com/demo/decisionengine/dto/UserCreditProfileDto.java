package com.demo.decisionengine.dto;

import com.demo.decisionengine.constants.UserCreditStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreditProfileDto {
    private UserCreditStatus userCreditStatus;
    private Integer creditModifier;

}
