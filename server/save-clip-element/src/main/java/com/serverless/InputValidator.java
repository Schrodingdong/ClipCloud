package com.serverless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputValidator {
    public static final List<String> INPUT_ESSENTIAL_FIELDS = Arrays.asList("created", "uuid", "type", "osVersion", "osName", "osArch", "userName","content");
    public static final List<String> INPUT_IMAGE_FIELDS = Arrays.asList("imgName", "tmpPath");
    public static final List<String> INPUT_FILE_FIELDS = Arrays.asList("srcPath", "tmpPath");

    public boolean validateEssentialInputFields(Map<String, Object> input){
        for (String field : INPUT_ESSENTIAL_FIELDS) {
            if (!input.containsKey(field)) {
                return false;
            }
        }
        return true;
    }

    public boolean validateTextInputFields(Map<String, Object> input){
        return validateEssentialInputFields(input);
    }

    public boolean validateImageInputFields(Map<String, Object> input){
        if(!validateEssentialInputFields(input)) return false;
        for(String field: INPUT_IMAGE_FIELDS){
            if(!input.containsKey(field)){
                return false;
            }
        }
        return true;
    }

    public boolean validateFileInputFields(Map<String, Object> input){
        if(!validateEssentialInputFields(input)) return false;
        for(String field: INPUT_FILE_FIELDS){
            if(!input.containsKey(field)){
                return false;
            }
        }
        return true;
    }





}
