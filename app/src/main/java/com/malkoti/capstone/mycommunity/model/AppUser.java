package com.malkoti.capstone.mycommunity.model;

public class AppUser {
    public String firstName;
    public String lastName;
    public String fullName;
    public boolean isManager;

    public AppUser() {
        // empty constructor
    }

    public AppUser(String firstName, String lastName, String fullName, boolean isManager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.isManager = isManager;
    }
}
