package com.serverless.retrieve_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.save_lambda.InputValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(com.serverless.save_lambda.Handler.class);
    private static final String TABLE_NAME = System.getenv("tableName");
    private final Region REGION = Region.EU_WEST_2;
    private final Map<String,String> headers = new HashMap<>();
    private DynamoDbClient amazonDynamoDbClient;

    // todo :  do paging request
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> apiInput, Context context) {
        // init client
        initSdkClients();
        initHeaders();

        // construct the request
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();
        ScanResponse response = amazonDynamoDbClient.scan(scanRequest);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString;
        JsonNode json;
        try {
            List<Map<String,String>> newItems = new ArrayList<>();

            for(Map<String, AttributeValue> item : response.items()){
                Map<String, String> newEl = new HashMap<>();
                for(Map.Entry<String, AttributeValue> el : item.entrySet()){
                    newEl.put(el.getKey(),el.getValue().s());
                }
                newItems.add(newEl);
            }
            jsonString = mapper.writeValueAsString(newItems);
            json = mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody("Error :" + e.getMessage())
                    .setHeaders(headers)
                    .build();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(json)
                .setHeaders(headers)
                .build();
    }



    private void initSdkClients() {
        this.amazonDynamoDbClient = DynamoDbClient.builder()
                .region(REGION)
                .build();
    }
    private void initHeaders(){
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "*");
    }

    private String getTableAttributesAsString(List<String> attributeList){
        StringBuilder s = new StringBuilder();
        for(String el : attributeList){
            if(el.equals("uuid")) el = "#uid";
            else if(el.equals("type")) el = "#tp";
            s.append(el).append(",");
        }
        String result = s.substring(0, s.length()-1);
        System.out.println(">>> Table attributes to get" + result);
        return result;
    }
}
