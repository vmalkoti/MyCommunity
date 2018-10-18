package com.malkoti.capstone.mycommunity.model;

public class MaintenanceRequest {
    public String reqType;
    public String unitName;
    public String residentName; // do we need this?
    public String reqStatus;
    public String reqDescription;
    public String reqComments;

    public MaintenanceRequest() {
        // empty constructor
    }

    public MaintenanceRequest(String reqType, String unitName, String residentName,
                              String reqStatus, String reqDescription, String reqComments) {
        this.reqType = reqType;
        this.unitName = unitName;
        this.residentName = residentName;
        this.reqStatus = reqStatus;
        this.reqDescription = reqDescription;
        this.reqComments = reqComments;
    }
}
