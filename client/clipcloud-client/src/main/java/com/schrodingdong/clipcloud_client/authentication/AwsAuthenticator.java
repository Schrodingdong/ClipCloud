package com.schrodingdong.clipcloud_client.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AwsAuthenticator extends AbstractAuthenticator{
    private static final String logInUrl = System.getenv("login_url");
    private static final String signUpUrl = System.getenv("signup_url");
    private static final URI logInUri = URI.create(logInUrl);
    private static final URI signUpUri = URI.create(signUpUrl);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String logIn(AuthModel authParam){
        String email = authParam.getEmail();
        String password = authParam.getPassword();
        // init HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // construct body
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("email", email);
        bodyMap.put("password", password);
        String jsonBody = "";
        try{
            jsonBody = new ObjectMapper().writeValueAsString(bodyMap);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        // Construct request
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(logInUri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send Request
        try {
            HttpResponse<?> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode inputJsonNode = mapper.readTree(httpResponse.body().toString()).get("input");
            String accessToken = inputJsonNode.get("accessToken").asText();
            String refreshToken = inputJsonNode.get("refreshToken").asText();
            String idToken = inputJsonNode.get("idToken").asText();
            setAccessToken(accessToken);
            setRefreshToken(refreshToken);
            setIdToken(idToken);
            return accessToken;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean signUp(AuthModel authParam){
        String email = authParam.getEmail();
        String password = authParam.getPassword();
        // init HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // construct body
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("email", email);
        bodyMap.put("password", password);
        String jsonBody = "";
        try{
            jsonBody = new ObjectMapper().writeValueAsString(bodyMap);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        // Construct request
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(signUpUri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send Request
        try {
            client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
