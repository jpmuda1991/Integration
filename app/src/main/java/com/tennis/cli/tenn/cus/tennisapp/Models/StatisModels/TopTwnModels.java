package com.tennis.cli.tenn.cus.tennisapp.Models.StatisModels;

public class TopTwnModels {

    private String name;
    private String dob;
    private String abbr;

    public TopTwnModels() {
    }


    public TopTwnModels(String name, String dob, String abbr) {
        this.name = name;
        this.dob = dob;
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}
