package com.shaukan.gabriel.caripacar.Cards;

//Creating Card class
public class Cards {
    //Card Object instance variables
    private String userId;
    private String name;
    private String profileImageUrl;
    private String occupation;
    private String age;
    private String notificationKey;

    //Card Object Constructor
    public Cards (String userId, String name, String profileImageUrl, String occupation, String age, String notificationKey) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.occupation = occupation;
        this.age = age;
        this.notificationKey = notificationKey;
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

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getNotificationKey() { return notificationKey; }

}
