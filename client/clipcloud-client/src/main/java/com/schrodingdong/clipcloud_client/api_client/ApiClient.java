package com.schrodingdong.clipcloud_client.api_client;

import com.schrodingdong.clipcloud_client.authentication.AuthenticationException;
import com.schrodingdong.clipcloud_client.authentication.RequestException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private HttpClient client;
    private static ApiClient instance = null;

    private ApiClient(){
        client = HttpClient.newHttpClient();
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public void sendRequest(HttpRequest request) throws AuthenticationException, RequestException {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()){
                case 401:
                    throw new AuthenticationException("Expired Access Token, needs relogin");
                case 400:
                    throw new RequestException("Invalid input : " + response.body());
            }
            System.out.println(">>> response body : \n\t" + response.body());
        } catch (IOException  | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
