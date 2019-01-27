package com.shaukan.gabriel.caripacar.Matches;

//create MatchesObject class
public class MatchesObject {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String mostRecentChat;

    public MatchesObject (String userId, String name, String profileImageUrl, String mostRecentChat) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.mostRecentChat = mostRecentChat;
    }

    public String getUserId() {
        return userId;
    }
    public  void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public  void setName(String name) {
        this.name = name;
    }

    public String getMostRecentChat() {
        return mostRecentChat;
    }
    public  void setMostRecentChat(String mostRecentChat) {
        this.mostRecentChat = mostRecentChat;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public  void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
