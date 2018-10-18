package com.malkoti.capstone.mycommunity.model;

public class Apartment {
    public String aptName;
    public int numOfBedrooms;
    public String status;
    public String aptDescription;

    public Apartment() {
        // empty constructor
    }

    public Apartment(String aptName, int numOfBedrooms, String status, String aptDescription) {
        this.aptName = aptName;
        this.numOfBedrooms = numOfBedrooms;
        this.status = status;
        this.aptDescription = aptDescription;
    }
}