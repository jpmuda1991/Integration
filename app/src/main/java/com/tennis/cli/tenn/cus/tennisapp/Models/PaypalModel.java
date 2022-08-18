package com.tennis.cli.tenn.cus.tennisapp.Models;

public class PaypalModel {

    private String email;
    private String password;


    public PaypalModel(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
