package com.malkoti.capstone.mycommunity.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Management {
    public String mgmtName;

    public String mgrId;

    public Management() {
        // empty constructor
    }

    public Management(String mgmtName) {
        this.mgmtName = mgmtName;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("mgmtName", mgmtName);
        result.put("mgrId", mgrId);

        return  result;
    }
}
