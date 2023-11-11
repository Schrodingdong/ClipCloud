package com.schrodingdong.clipcloud_client.authentication;

public class AuthModel {
    private String email;
    private String password;

    public AuthModel(){
        this.email = null;
        this.password = null;
    }
    public AuthModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
