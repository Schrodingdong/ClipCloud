package com.schrodingdong.clipcloud_client.authentication;

public class RequestException extends Exception{
    public RequestException(String errorMessage){
        super(errorMessage);
    }
    public RequestException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
