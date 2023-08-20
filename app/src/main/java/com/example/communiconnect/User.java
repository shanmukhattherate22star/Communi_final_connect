package com.example.communiconnect;

import java.io.Serializable;





public class User implements Serializable {
    private String username;
    private String email;

    private LocationInfo locationinfo;
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.locationinfo = new LocationInfo(0, 0, "", "");
    }
    public void addlocationinfo(double latitude,double longitude,String title,String description){
        if (this.locationinfo == null) {
            this.locationinfo = new LocationInfo(latitude, longitude, title, description);
        } else {
            this.locationinfo.setLatitude(latitude);
            this.locationinfo.setLongitude(longitude);
            this.locationinfo.setTitle(title);
            this.locationinfo.setDescription(description);
        }
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getTitle(){
        return locationinfo.getTitle();
    }
    public String getDescription(){
        return locationinfo.getDescription();
    }
}
