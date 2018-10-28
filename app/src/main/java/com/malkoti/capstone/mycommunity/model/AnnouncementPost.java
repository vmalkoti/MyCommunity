package com.malkoti.capstone.mycommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class AnnouncementPost implements Parcelable {
    public String announcementTitle;
    public String managerId;
    public String communityId;
    public String postDate;
    public String postDescription;

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
     * Parcelable implementation
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
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(announcementTitle);
        dest.writeString(managerId);
        dest.writeString(communityId);
        dest.writeString(postDate);
        dest.writeString(postDescription);
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
}
