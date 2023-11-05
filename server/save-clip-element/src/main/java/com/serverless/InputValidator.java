package com.serverless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputValidator {
    private final List<String> inputEssentialFields = new ArrayList<>();
    private final List<String> inputImageFields = new ArrayList<>();
    private final List<String> inputFileFields = new ArrayList<>();


    public InputValidator(){
        inputEssentialFields.addAll(Arrays.asList(
                "created", "uuid", "type", "osVersion", "osName", "osArch", "userName","content"
        ));
        inputImageFields.addAll(Arrays.asList("srcPath", "tmpPath"));
        inputFileFields.addAll(Arrays.asList("srcPath", "tmpPath"));
    }

    public boolean validateEssentialInputFields(Map<String, Object> input){
        for (String field : inputEssentialFields) {
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
        for(String field: inputImageFields){
            if(!input.containsKey(field)){
                return false;
            }
        }
        return true;
    }

    public boolean validateFileInputFields(Map<String, Object> input){
        if(!validateEssentialInputFields(input)) return false;
        for(String field: inputFileFields){
            if(!input.containsKey(field)){
                return false;
            }
        }
        return true;
    }





}
