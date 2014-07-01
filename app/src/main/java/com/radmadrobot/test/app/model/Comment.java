package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toker on 6/15/2014.
 */
public class Comment {

    @SerializedName("id")
    private long mId;

    @SerializedName("created_time")
    private long mCreatedTime;

    @SerializedName("text")
    private String mText;

    @SerializedName("from")
    private User mUserFrom;
}
