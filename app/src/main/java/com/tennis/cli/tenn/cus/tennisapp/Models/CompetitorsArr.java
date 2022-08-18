package com.tennis.cli.tenn.cus.tennisapp.Models;

public class CompetitorsArr {

    private int rank;
    private int movement;
    private int points;
    private int competition_played;
    private Competitor competitor;


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCompetition_played() {
        return competition_played;
    }

    public void setCompetition_played(int competition_played) {
        this.competition_played = competition_played;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }
}
