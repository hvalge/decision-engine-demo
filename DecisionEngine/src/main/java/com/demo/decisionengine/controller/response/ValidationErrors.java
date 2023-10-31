// Author: Märt Kalmo
// https://bitbucket.org/mkalmo/icd0011tests

package com.demo.decisionengine.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class ValidationErrors {

    private List<ValidationError> errors;

    public void addFieldError(FieldError fieldError) {
        List<String> args = Stream.of(fieldError.getArguments())
                .filter(arg -> !(arg instanceof DefaultMessageSourceResolvable))
                .map(String::valueOf)
                .collect(Collectors.toList());

        errors.add(new ValidationError(fieldError.getCodes()[0], args));
    }
}
