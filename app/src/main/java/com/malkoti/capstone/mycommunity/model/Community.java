package com.malkoti.capstone.mycommunity.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Community {
    public String name;
    public String description;
    public String streetAddress;
    public String city;
    public String state;
    public String zip;
    public String country;
    public String phoneNum;

    public String mgmtId;
    public String mgrId;

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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("description", description);
        result.put("streetAddress", streetAddress);
        result.put("city", city);
        result.put("state", state);
        result.put("zip", zip);
        result.put("country", country);
        result.put("phoneNum", phoneNum);
        result.put("mgmtId", mgmtId);
        result.put("mgrId", mgrId);

        return  result;
    }
}
