package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
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
    @Exclude
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
    @Exclude
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

        result.put("aptName", aptName);
        result.put("numOfBedrooms", numOfBedrooms);
        result.put("status", status);
        result.put("communityId", communityId);
        result.put("aptDescription", aptDescription);

        return result;
    }

    @Exclude
    public static Apartment getDummyObject() {
        String emptyString = "";
        return new Apartment(emptyString, 0, emptyString,emptyString,emptyString);
    }

    @Exclude
    @Override
    public String toString() {
        return this.aptName;
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof Apartment)) return false;

        Apartment apt = (Apartment) obj;

        Log.d("DEBUG_Apartment", "Key of this=" + this.aptKey + ", passed object key=" + apt.aptKey);
        return (this.aptKey.equals(apt.aptKey));
    }
}