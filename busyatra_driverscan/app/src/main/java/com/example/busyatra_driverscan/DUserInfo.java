package com.example.busyatra_driverscan;

public class DUserInfo {

    private String userType;
    private String userName;

    private String userAge;

    private String userCity;


    public DUserInfo() {

    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String type) {
        this.userType = type;
    }


    public String getUserName () {
        return  userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getUserAge() {
        return  userAge;
    }

    public void setUserAge(String des) {
        this.userAge = des;
    }

    public String getUserCity() {
        return  userCity;
    }

    public void setUserCity(String fov) {
        this.userCity = fov;
    }

}
