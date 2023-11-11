package com.schrodingdong.clipcloud_client.authentication;

public class AuthenticationException extends Exception{
    public AuthenticationException(String errorMessage){
        super(errorMessage);
    }
    public AuthenticationException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
