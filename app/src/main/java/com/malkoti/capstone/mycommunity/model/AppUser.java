package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class AppUser implements Parcelable {
    //public String firstName;
    //public String lastName;
    public String fullName;
    public boolean isManager;
    public String gender;
    public String email;
    public String phoneNum;

    public String mgmtId;
    public String communityId;
    public String aptId;

    public AppUser() {
        // empty constructor
    }

    public AppUser(//String firstName, String lastName,
                   String fullName, boolean isManager,
                   String gender, String email, String phone,
                   String mgmtId, String communityId, String aptId) {
        //this.firstName = firstName;
        //this.lastName = lastName;
        this.fullName = fullName;
        this.isManager = isManager;
        this.gender = gender;
        this.email = email;
        this.phoneNum = phone;

        // reference ids
        this.mgmtId = mgmtId;  // for manager of a management
        this.communityId = communityId;  // for manager of a community
        this.aptId = aptId;  // for a resident
    }

    /*
     * Parcelable implementation
     */
    protected AppUser(Parcel in) {
        fullName = in.readString();
        isManager = in.readByte() != 0;
        gender = in.readString();
        email = in.readString();
        phoneNum = in.readString();
        mgmtId = in.readString();
        communityId = in.readString();
        aptId = in.readString();
    }

    public static AppUser getDummyObject() {
        return new AppUser("", false, "", "", "", "", "", "");
    }

    /*
     * Parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeByte((byte) (isManager ? 1 : 0));
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(phoneNum);
        dest.writeString(mgmtId);
        dest.writeString(communityId);
        dest.writeString(aptId);
    }

    /*
     * Parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * Parcelable implementation
     */
    public static final Creator<AppUser> CREATOR = new Creator<AppUser>() {
        @Override
        public AppUser createFromParcel(Parcel in) {
            return new AppUser(in);
        }

        @Override
        public AppUser[] newArray(int size) {
            return new AppUser[size];
        }
    };

    /**
     * To be used when implementing Firebase realtime db fan-out approach.
     * Or when writing Firebase realtime node to a specified push key.
     * @return A map object containing key-value pairs of object values
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("fullName", fullName);
        result.put("isManager", isManager);
        result.put("gender", gender);
        result.put("email", email);
        result.put("phoneNum", phoneNum);
        // reference keys
        result.put("mgmtId", mgmtId == null ? "" : mgmtId);
        result.put("communityId", communityId == null ? "" : communityId);
        result.put("aptId", aptId == null ? "" : aptId);

        return result;
    }
}
