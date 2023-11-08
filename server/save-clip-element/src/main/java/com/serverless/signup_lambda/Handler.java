package com.serverless.signup_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

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
        // Initialize Cognito client
        initHeaders();
        initSdkClients();

        // Extract request body
        Map<String, Object> input = null;
        try {
            String sInput = (String) apiInput.get("body");
            input = new ObjectMapper().readValue(sInput, Map.class);
        } catch (Exception e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(e.getMessage())
                    .build();
        }

        // Get user attributes
        String email = (String) input.get("email");
        String password = (String) input.get("password");

        // Perform user signup
        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("email", email);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(APP_CLIENT_ID)
                .username(email)
                .password(password)
                .build();
        try {
            SignUpResponse signUpResponse = cognitoClient.signUp(signUpRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(e.getMessage())
                    .build();
        }

        // confirm the user
        AdminConfirmSignUpRequest adminConfirmSignUpRequest = AdminConfirmSignUpRequest.builder()
                .username(email)
                .userPoolId(USER_POOL_ID)
                .build();

        try {
            cognitoClient.adminConfirmSignUp(adminConfirmSignUpRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(e.getMessage())
                    .build();
        }

        // You can handle the response based on your requirements
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody("User signup successful")
                .build();
    }

    private void initSdkClients(){
        cognitoClient = CognitoIdentityProviderClient.builder()
                .region(REGION)
                .build();
    }
    private void initHeaders() {
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "*");
    }
}
