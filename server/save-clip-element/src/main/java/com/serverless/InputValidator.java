package com.serverless;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputValidator {
    public static final List<String> INPUT_ESSENTIAL_FIELDS = Arrays.asList("contentBase64","created", "uuid", "type", "osVersion", "osName", "osArch", "userName");

    public boolean validateEssentialInputFields(Map<String, Object> input){
        for (String field : INPUT_ESSENTIAL_FIELDS) {
            if (!input.containsKey(field)) {
                return false;
            }
        }
        return true;
    }
}
