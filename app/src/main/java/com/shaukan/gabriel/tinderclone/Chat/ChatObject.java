package com.shaukan.gabriel.tinderclone.Chat;

//Create chatObject class
public class ChatObject {
    //chatObject instance variables
    private String message;
    private Boolean currentUser;

    //chatObject constructor
    public ChatObject(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    //chatObject methods
    public String getMessage() {
        return message;
    }
    public  void setMessage(String userId) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }
    public  void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }

}
