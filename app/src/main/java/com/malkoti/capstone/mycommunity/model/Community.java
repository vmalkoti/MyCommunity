package com.malkoti.capstone.mycommunity.model;

public class Community {
    public String name;
    public String description;
    public String streetAddress;
    public String city;
    public String state;
    public String zip;
    public String country;
    public String phoneNum;

    public Community() {
        // empty constructor
    }

    public Community(String communityName, String description, String address,
                     String city, String state, String zip, String country, String phone) {
        this.name = communityName;
        this.description = description;
        this.streetAddress = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phoneNum = phone;
    }
}
