package com.example.jiabaotan2012.cw;

/**
 * Created by jiabao.tan.2012 on 26/7/2015.
 */
public class Account {

    private String email;
    private String name;
    private String password;
    private String repassword;
    private String type;

    public Account() {}

    public Account(String email, String name, String password, String repassword, String type) {
        this.setEmail(email);
        this.setName(name);
        this.setPassword(password);
        this.setRepassword(repassword);
        this.setType(type);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
