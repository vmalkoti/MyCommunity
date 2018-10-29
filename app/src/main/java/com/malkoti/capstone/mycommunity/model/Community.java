package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Community implements Parcelable {
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


    /*
     * Parcelable implementation
     */
    public static final Creator<Community> CREATOR = new Creator<Community>() {
        @Override
        public Community createFromParcel(Parcel in) {
            return new Community(in);
        }

        @Override
        public Community[] newArray(int size) {
            return new Community[size];
        }
    };


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

    /*
     * Parcelable implementation
     */
    protected Community(Parcel in) {
        name = in.readString();
        description = in.readString();
        streetAddress = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        country = in.readString();
        phoneNum = in.readString();
        mgmtId = in.readString();
        mgrId = in.readString();
    }



    /*
     * Parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(streetAddress);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(country);
        dest.writeString(phoneNum);
        dest.writeString(mgmtId);
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
     * @return A map object containing key-value pairs of object values
     */
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


    public static Community getDummyObject() {
        return new Community("", "", "", "", "", "", "", "");
    }
}
