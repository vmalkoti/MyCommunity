package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Apartment implements Parcelable {
    public String aptName;
    public int numOfBedrooms;
    public String status;
    public String communityId;
    public String aptDescription;

    @Exclude
    public String aptKey;

    /*
     * Parcelable implementation
     */
    public static final Creator<Apartment> CREATOR = new Creator<Apartment>() {
        @Override
        public Apartment createFromParcel(Parcel in) {
            return new Apartment(in);
        }

        @Override
        public Apartment[] newArray(int size) {
            return new Apartment[size];
        }
    };


    public Apartment() {
        // empty constructor
    }

    public Apartment(String aptName, int numOfBedrooms, String status, String communityId, String aptDescription) {
        this.aptName = aptName;
        this.numOfBedrooms = numOfBedrooms;
        this.status = status;
        this.communityId = communityId;
        this.aptDescription = aptDescription;
    }

    /*
     * Parcelable implementation
     */
    protected Apartment(Parcel in) {
        aptName = in.readString();
        numOfBedrooms = in.readInt();
        status = in.readString();
        communityId = in.readString();
        aptDescription = in.readString();
    }

    /*
     * Parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aptName);
        dest.writeInt(numOfBedrooms);
        dest.writeString(status);
        dest.writeString(communityId);
        dest.writeString(aptDescription);
    }

    /*
     * Parcelable implementation
     */
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

        result.put("aptName", aptName);
        result.put("numOfBedrooms", numOfBedrooms);
        result.put("status", status);
        result.put("communityId", communityId);
        result.put("aptDescription", aptDescription);

        return result;
    }

    public static Apartment getDummyObject() {
        return new Apartment("", 1, "","","");
    }
}