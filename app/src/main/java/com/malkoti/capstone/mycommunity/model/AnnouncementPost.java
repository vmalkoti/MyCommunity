package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class AnnouncementPost implements Parcelable {
    public String announcementTitle;
    public String managerId;
    public String communityId;
    public String postDate;
    public String postDescription;

    @Exclude
    public String postKey;


    /*
     * Parcelable implementation
     */
    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * Parcelable implementation
     */
    @Exclude
    public static final Creator<AnnouncementPost> CREATOR = new Creator<AnnouncementPost>() {
        @Override
        public AnnouncementPost createFromParcel(Parcel in) {
            return new AnnouncementPost(in);
        }

        @Override
        public AnnouncementPost[] newArray(int size) {
            return new AnnouncementPost[size];
        }
    };


    public AnnouncementPost() {
        // empty constructor
    }

    public AnnouncementPost(String announcementTitle, String postedBy, String communityId,
                            String postDate, String postDescription) {
        this.announcementTitle = announcementTitle;
        this.managerId = postedBy;
        this.communityId = communityId;
        this.postDate = postDate;
        this.postDescription = postDescription;
    }

    /*
     * Parcelable implementation constructor
     */
    protected AnnouncementPost(Parcel in) {
        announcementTitle = in.readString();
        managerId = in.readString();
        communityId = in.readString();
        postDate = in.readString();
        postDescription = in.readString();
    }


    /*
     * Parcelable implementation
     */
    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(announcementTitle);
        dest.writeString(managerId);
        dest.writeString(communityId);
        dest.writeString(postDate);
        dest.writeString(postDescription);
    }



    /**
     * To be used when implementing Firebase realtime db fan-out approach.
     * Or when writing Firebase realtime node to a specified push key.
     * @return A map object containing key-value pairs of object values
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("announcementTitle", announcementTitle);
        result.put("managerId", managerId);
        result.put("communityId", communityId);
        result.put("postDate", postDate);
        result.put("postDescription", postDescription);

        return result;
    }

    @Exclude
    public static AnnouncementPost getDummyObject() {
        String emptyString = "";
        return new AnnouncementPost(emptyString, emptyString, emptyString, emptyString, emptyString);
    }
}
