package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class MaintenanceRequest implements Parcelable {
    public String reqType;
    public String communityId;
    public String aptId;
    public String residentId; // do we need this?
    public String reqDate;
    public String reqStatus;
    public String reqDescription;
    public String reqComments;

    @Exclude
    public String reqKey;

    @Exclude
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
                              String reqDate,
                              String reqStatus, String reqDescription, String reqComments) {
        this.reqType = reqType;
        this.communityId = communityId;
        this.aptId = aptId;
        this.residentId = residentId;
        this.reqDate = reqDate;
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
        reqDate = in.readString();
        reqStatus = in.readString();
        reqDescription = in.readString();
        reqComments = in.readString();
    }

    /*
     * Parcelable implementation
     */
    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reqType);
        dest.writeString(communityId);
        dest.writeString(aptId);
        dest.writeString(residentId);
        dest.writeString(reqDate);
        dest.writeString(reqStatus);
        dest.writeString(reqDescription);
        dest.writeString(reqComments);
    }

    /*
     * Parcelable implementation
     */
    @Exclude
    @Override
    public int describeContents() {
        return 0;
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
        result.put("reqDate", reqDate);
        result.put("reqStatus", reqStatus);
        result.put("reqDescription", reqDescription);
        result.put("reqComments", reqComments);

        return  result;
    }

    @Exclude
    public static MaintenanceRequest getDummyObject() {
        return new MaintenanceRequest("", "", "", "", "", "", "", "");
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof MaintenanceRequest)) return false;

        MaintenanceRequest req = (MaintenanceRequest) obj;

        return (this.reqKey.equals(req.reqKey));
    }
}
