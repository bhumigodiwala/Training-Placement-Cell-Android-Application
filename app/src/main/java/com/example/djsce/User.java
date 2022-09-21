package com.example.djsce;

public class User {
    String password;
    String type;
    String userid;
    boolean isComplete;
    boolean isVerified;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean getisComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean getisVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public User(String password, String type, String userid, boolean isComplete, boolean isVerified) {
        this.password = password;
        this.type = type;
        this.userid = userid;
        this.isComplete = isComplete;
        this.isVerified = isVerified;
    }

    public User(){

    }
}
