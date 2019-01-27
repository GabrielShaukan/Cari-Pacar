package com.shaukan.gabriel.caripacar.Chat;

//Create chatObject class
public class ChatObject {
    //chatObject instance variables
    private String message;
    private String currentTime;
    private Boolean currentUser;

    //chatObject constructor
    public ChatObject(String message, String currentTime, Boolean currentUser) {
        this.message = message;
        this.currentTime = currentTime;
        this.currentUser = currentUser;
    }

    //chatObject methods
    public String getMessage() {
        return message;
    }
    public  void setMessage(String userId) {
        this.message = message;
    }

    public String getCurrentTime() {
        return currentTime;
    }
    public void setCurrentTime (String currentTime) {
        this.currentTime = currentTime;
    }


    public Boolean getCurrentUser() {
        return currentUser;
    }
    public  void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }

}
