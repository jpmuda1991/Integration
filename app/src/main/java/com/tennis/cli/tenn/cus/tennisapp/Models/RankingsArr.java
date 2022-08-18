package com.tennis.cli.tenn.cus.tennisapp.Models;

import java.util.List;

public class RankingsArr {

    private  int type_id;
    private String name;
    private int year;
    private int week;
    private String gender;
    private List<CompetitorsArr> competitor_rankings;


    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<CompetitorsArr> getCompetitor_rankings() {
        return competitor_rankings;
    }

    public void setCompetitor_rankings(List<CompetitorsArr> competitor_rankings) {
        this.competitor_rankings = competitor_rankings;
    }
}
