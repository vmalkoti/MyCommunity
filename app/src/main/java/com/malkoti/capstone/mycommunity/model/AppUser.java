package com.malkoti.capstone.mycommunity.model;

public class AppUser {
    public String firstName;
    public String lastName;
    public String fullName;
    public boolean isManager;
    public String gender;
    public String email;
    public String phoneNum;

    public AppUser() {
        // empty constructor
    }

    public AppUser(String firstName, String lastName, String fullName, boolean isManager,
                   String gender, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.isManager = isManager;
        this.gender = gender;
        this.email = email;
        this.phoneNum = phone;
    }
}
