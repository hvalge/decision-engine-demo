// Author: Märt Kalmo
// https://bitbucket.org/mkalmo/icd0011tests

package com.demo.decisionengine.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ValidationError {

    @NonNull
    private String code;

    @JsonInclude(Include.NON_NULL)
    private List<String> arguments;

}
