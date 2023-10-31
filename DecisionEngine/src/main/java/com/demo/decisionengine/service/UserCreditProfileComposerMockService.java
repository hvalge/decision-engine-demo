package com.demo.decisionengine.service;

import com.demo.decisionengine.constants.UserCreditStatus;
import com.demo.decisionengine.dto.UserCreditProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserCreditProfileComposerMockService {

    private static final Logger logger = LoggerFactory.getLogger(UserCreditProfileComposerMockService.class);

    private static final Map<String, UserCreditProfileDto> USER_PROFILES;

    static {
        USER_PROFILES = new HashMap<>();
        USER_PROFILES.put("49002010965", new UserCreditProfileDto(UserCreditStatus.IN_DEBT, null));
        USER_PROFILES.put("49002010976", new UserCreditProfileDto(UserCreditStatus.ELIGIBLE, 100));
        USER_PROFILES.put("49002010987", new UserCreditProfileDto(UserCreditStatus.ELIGIBLE, 300));
        USER_PROFILES.put("49002010998", new UserCreditProfileDto(UserCreditStatus.ELIGIBLE, 1000));
    }

    public UserCreditProfileDto getUserCreditModifier(String personalCode) {
        logger.info("Getting mock result for personalCode {}", personalCode);
        UserCreditProfileDto profile = USER_PROFILES.get(personalCode);
        if (profile == null) {
            throw new IllegalArgumentException(String.format("No matching user credit profile mock for personal code '%s'", personalCode));
        }
        return profile;
    }
}
