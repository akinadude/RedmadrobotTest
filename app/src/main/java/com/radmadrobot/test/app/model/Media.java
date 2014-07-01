package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by toker on 6/15/2014.
 */
public class Media {

    @SerializedName("id")
    private String mId;

    @SerializedName("attribution")
    private String mAttribution;

    @SerializedName("tags")
    private LinkedList<String> mTags;

    //TODO заменить на enum
    @SerializedName("type")
    private String mType;

    @SerializedName("location")
    private Location mLocation;

    @SerializedName("comments")
    private Comments mComments;

    @SerializedName("filter")
    private String mFilter;

    @SerializedName("created_time")
    private String mCreatedTime;

    @SerializedName("link")
    private String mLink;

    @SerializedName("likes")
    private Likes mLikes;

    @SerializedName("images")
    private Images mImages;

    @SerializedName("videos")
    private Videos mVideos;

    @SerializedName("users_in_photo")
    private LinkedList<User> mUsersInPhoto;

    @SerializedName("caption")
    private Caption mCaption;

    @SerializedName("user_has_liked")
    private boolean mUserHasLiked;

    @SerializedName("user")
    private User mUser;


    public String getId(){
        return mId;
    }

    public Images getImages() {
        if (mImages != null)
            return mImages;
        else
            return new Images();
    }
}
