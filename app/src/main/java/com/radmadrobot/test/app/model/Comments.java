package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by toker on 6/15/2014.
 */
public class Comments extends Entity {

    @SerializedName("count")
    private long mCount;

    @SerializedName("date")
    private LinkedList<Comment> mComments;
}
