package com.tennis.cli.tenn.cus.tennisapp.Models;

public class PlayersModel {

    private String player_id;
    private String profileImg;
    private String flatImg;
    private String name;
    private String age;
    private String points;
    private String country;
    private String gender;

    public PlayersModel(String player_id,String profileImg, String flatImg, String name, String age, String points,String country,String gender) {
        this.player_id = player_id;
        this.profileImg = profileImg;
        this.flatImg = flatImg;
        this.name = name;
        this.age = age;
        this.points = points;
        this.country = country;
        this.gender = gender;
    }


    public String getPlayer_id() {
        return player_id;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getFlatImg() {
        return flatImg;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getPoints() {
        return points;
    }
}
