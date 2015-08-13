package com.android.clockwork.model;

/**
 * Created by Hoi Chuen on 10/8/2015.
 */
public class Session {
    private int id;
    private String userName;
    private String email;
    private String password;
    private String account_type;
    private String authentication_token;

    public Session(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Session(int id, String userName, String email, String account_type, String password, String authentication_token) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.account_type = account_type;
        this.password = password;
        this.authentication_token = authentication_token;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String name) {

        this.userName = name;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return account_type;
    }

    public void setAccountType(String accountType) {
        this.account_type = account_type;
    }

    public String getAuthenticationToken(){
        return authentication_token;
    }

    public void setAuthenticationToken(String authentication_token){
        this.authentication_token = authentication_token;
    }
}
