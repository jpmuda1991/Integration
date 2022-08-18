package com.tennis.cli.tenn.cus.tennisapp.Models;

public class CreditModel {

    private String type;
    private String card_nmber;
    private String mm;
    private String yy;
    private String cvv;
    private String fullname;
    private String address;


    public CreditModel(String type, String card_nmber, String mm, String yy, String cvv, String fullname, String address) {
        this.type = type;
        this.card_nmber = card_nmber;
        this.mm = mm;
        this.yy = yy;
        this.cvv = cvv;
        this.fullname = fullname;
        this.address = address;
    }


    public String getType() {
        return type;
    }

    public String getCard_nmber() {
        return card_nmber;
    }

    public String getMm() {
        return mm;
    }

    public String getYy() {
        return yy;
    }

    public String getCvv() {
        return cvv;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }
}
