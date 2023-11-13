package com.serverless.login_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(com.serverless.save_lambda.Handler.class);
    private final Map<String,String> headers = new HashMap<>();
    private CognitoIdentityProviderClient cognitoClient;
    private final Region REGION = Region.EU_WEST_2;

    private final static String USER_POOL_ID = System.getenv("userPoolId");
    private final static String APP_CLIENT_ID = System.getenv("appClientId");


    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> apiInput, Context context) {
        // init methods
        initHeaders();
        initSdkClients();



        // extract request body (don't forget to parse the string to a map
        Map<String, Object> input = null;
        try{
            String sInput = (String) apiInput.get("body");
            input =  new ObjectMapper().readValue(sInput, Map.class);
        } catch (Exception e) {
            Response responseBody = new Response(e.getMessage(), input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }

        // get email and password
        String email = (String) input.get("email");
        String password = (String) input.get("password");
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", email);
        authParams.put("PASSWORD", password);
        authParams.put("scope", "email");

        // authenticate user
        InitiateAuthRequest initiateAuthRequest = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(APP_CLIENT_ID)
                .authParameters(authParams)
                .build();

        try {
            InitiateAuthResponse authResult = cognitoClient.initiateAuth(initiateAuthRequest);
            LOG.info(">>> authResult: \n{}", authResult);
            String accessToken = authResult.authenticationResult().accessToken();
            String idToken = authResult.authenticationResult().idToken();
            String refreshToken = authResult.authenticationResult().refreshToken();
            Map<String,Object> accessTokenMap= new HashMap<>();
            accessTokenMap.put("accessToken", accessToken);
            accessTokenMap.put("idToken", idToken);
            accessTokenMap.put("refreshToken", refreshToken);

            Response responseBody = new Response("Authentication successful", accessTokenMap);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            Response responseBody = new Response(e.getMessage(), input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
    private void initHeaders() {
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "*");
    }
    private void initSdkClients(){
        cognitoClient = CognitoIdentityProviderClient.builder()
                .region(REGION)
                .build();
    }
}
