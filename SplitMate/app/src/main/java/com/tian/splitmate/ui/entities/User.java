package com.tian.splitmate.ui.entities;

public class User {
    private int user_ID;
    private String UserName;
    private String PhoneNumber;

    public User(int user_ID, String userName, String phoneNumber) {
        this.user_ID = user_ID;
        UserName = userName;
        PhoneNumber = phoneNumber;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "user_ID=" + user_ID +
                ", UserName='" + UserName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                '}';
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
