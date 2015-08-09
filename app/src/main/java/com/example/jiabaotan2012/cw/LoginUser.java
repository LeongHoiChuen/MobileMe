package com.example.jiabaotan2012.cw;

/**
 * Created by Hoi Chuen on 2/8/2015.
 */
public class LoginUser {

    private String userName;
    private String passWord;

    public LoginUser () {}

    public LoginUser (String userName, String passWord) {
        this.setName(userName);
        this.setPassword(passWord);
    }

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }
}
