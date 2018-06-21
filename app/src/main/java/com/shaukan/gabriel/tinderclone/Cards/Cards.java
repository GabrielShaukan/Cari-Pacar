package com.shaukan.gabriel.tinderclone.Cards;

//Creating Card class
public class Cards {
    //Card Object instance variables
    private String userId;
    private String name;
    private String profileImageUrl;

    //Card Object Constructor
    public Cards (String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    //Card Object Methods
    public String getUserId() {
        return userId;
    }
    public  void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

}
