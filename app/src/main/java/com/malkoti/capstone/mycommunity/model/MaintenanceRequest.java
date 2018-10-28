package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MaintenanceRequest implements Parcelable {
    public String reqType;
    public String communityId;
    public String aptId;
    public String residentId; // do we need this?
    public String reqStatus;
    public String reqDescription;
    public String reqComments;


    /*
     * Parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MaintenanceRequest> CREATOR = new Creator<MaintenanceRequest>() {
        @Override
        public MaintenanceRequest createFromParcel(Parcel in) {
            return new MaintenanceRequest(in);
        }

        @Override
        public MaintenanceRequest[] newArray(int size) {
            return new MaintenanceRequest[size];
        }
    };


    public MaintenanceRequest() {
        // empty constructor
    }

    public MaintenanceRequest(String reqType, String communityId, String aptId, String residentId,
                              String reqStatus, String reqDescription, String reqComments) {
        this.reqType = reqType;
        this.communityId = communityId;
        this.aptId = aptId;
        this.residentId = residentId;
        this.reqStatus = reqStatus;
        this.reqDescription = reqDescription;
        this.reqComments = reqComments;
    }

    /*
     * Parcelable implementation
     */
    protected MaintenanceRequest(Parcel in) {
        reqType = in.readString();
        communityId = in.readString();
        aptId = in.readString();
        residentId = in.readString();
        reqStatus = in.readString();
        reqDescription = in.readString();
        reqComments = in.readString();
    }

    /*
     * Parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reqType);
        dest.writeString(communityId);
        dest.writeString(aptId);
        dest.writeString(residentId);
        dest.writeString(reqStatus);
        dest.writeString(reqDescription);
        dest.writeString(reqComments);
    }


    /**
     * To be used when implementing Firebase realtime db fan-out approach.
     * Or when writing Firebase realtime node to a specified push key.
     * @return A map object containing key-value pairs of object values
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("reqType", reqType);
        result.put("communityId", communityId);
        result.put("aptId", aptId);
        result.put("residentId", residentId);
        result.put("reqStatus", reqStatus);
        result.put("reqDescription", reqDescription);
        result.put("reqComments", reqComments);

        return  result;
    }
}
