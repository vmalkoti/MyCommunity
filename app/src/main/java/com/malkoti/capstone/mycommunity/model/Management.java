package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Management implements Parcelable {
    public String mgmtName;
    public String mgrId;

    /*
     * Parcelable implementation
     */
    public static final Creator<Management> CREATOR = new Creator<Management>() {
        @Override
        public Management createFromParcel(Parcel in) {
            return new Management(in);
        }

        @Override
        public Management[] newArray(int size) {
            return new Management[size];
        }
    };


    public Management() {
        // empty constructor
    }

    public Management(String mgmtName) {
        this.mgmtName = mgmtName;
    }


    /*
     * Parcelable implementation
     */
    protected Management(Parcel in) {
        mgmtName = in.readString();
        mgrId = in.readString();
    }

    /*
     * Parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mgmtName);
        dest.writeString(mgrId);
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
     * @return A map object containing key-value pairs of object properties
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("mgmtName", mgmtName);
        result.put("mgrId", mgrId);

        return  result;
    }
}
