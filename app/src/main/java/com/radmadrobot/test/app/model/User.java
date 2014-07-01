package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toker on 6/15/2014.
 */
public class User extends Entity {

    @SerializedName("id")
    private long mId;

    @SerializedName("username")
    private String mNickname;

    @SerializedName("full_name")
    private String mFullname;

    @SerializedName("profile_picture")
    private String mAvatarUrl;

    @SerializedName("bio")
    private String mBio;

    @SerializedName("website")
    private String mWebsite;

    @SerializedName("counts")
    private UserCounts mCounts;
}