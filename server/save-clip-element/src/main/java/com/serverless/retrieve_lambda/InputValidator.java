package com.serverless.retrieve_lambda;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputValidator {
    public final static List<String> INPUT_ESSENTIAL_FIELDS = Arrays.asList("");
    public boolean validateEssentialInputFields(Map<String, Object> input){
        for (String field : INPUT_ESSENTIAL_FIELDS) {
            if (!input.containsKey(field)) {
                return false;
            }
        }
        return true;
    }
}
