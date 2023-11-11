package com.schrodingdong.clipcloud_client.authentication;

public abstract class AbstractAuthenticator {
    private static String accessToken;
    private static String refreshToken;
    private static String idToken;
    public static void setAccessToken(String token){
        accessToken = token;
    }
    public static String getAccessToken(){
        return accessToken;
    }
    public static void setIdToken(String token){
        idToken = token;
    }
    public static String getIdToken(){
        return idToken;
    }
    public static void setRefreshToken(String token){
        refreshToken = token;
    }
    public static String getRefreshToken(){
        return refreshToken;
    }

    public abstract String logIn(AuthModel authParam);
    public abstract boolean signUp(AuthModel authParam);
}
