package com.malkoti.capstone.mycommunity.model;

public class AnnouncementPost {
    public String announcementTitle;
    public String postedBy;
    public String postDate;
    public String postDescription;

    public AnnouncementPost() {
        // empty constructor
    }

    public AnnouncementPost(String announcementTitle, String postedBy, String postDate, String postDescription) {
        this.announcementTitle = announcementTitle;
        this.postedBy = postedBy;
        this.postDate = postDate;
        this.postDescription = postDescription;
    }
}
