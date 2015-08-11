package com.android.clockwork.model;

/**
 * Created by Hoi Chuen on 10/8/2015.
 */
public class Session {
    private int id;
    private String userName;
    private String email;
    private String passWord;
    private String accountType;
    private String authenticationToken;

    public Session(String email, String passWord) {
        this.email = email;
        this.passWord = passWord;
    }

    public Session(int id, String userName, String email, String accountType, String passWord, String authenticationToken) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.accountType = accountType;
        this.passWord = passWord;
        this.authenticationToken = authenticationToken;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String name) {

        this.userName = name;
    }

    public String getPassword() {

        return passWord;
    }

    public void setPassword(String password) {

        this.passWord = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAuthenticationToken(){
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken){
        this.authenticationToken = authenticationToken;
    }
}
