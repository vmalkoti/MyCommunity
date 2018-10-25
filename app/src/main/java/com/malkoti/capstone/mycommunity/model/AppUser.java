package com.malkoti.capstone.mycommunity.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class AppUser {
    //public String firstName;
    //public String lastName;
    public String fullName;
    public boolean isManager;
    public String gender;
    public String email;
    public String phoneNum;

    public String communityId;
    public String aptId;

    public AppUser() {
        // empty constructor
    }

    public AppUser(//String firstName, String lastName,
                   String fullName, boolean isManager,
                   String gender, String email, String phone) {
        //this.firstName = firstName;
        //this.lastName = lastName;
        this.fullName = fullName;
        this.isManager = isManager;
        this.gender = gender;
        this.email = email;
        this.phoneNum = phone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("fullName", fullName);
        result.put("isManager", isManager);
        result.put("gender", gender);
        result.put("email", email);
        result.put("phoneNum", phoneNum);
        result.put("communityId", communityId);
        result.put("aptId", aptId);

        return result;
    }
}
