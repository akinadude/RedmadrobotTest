package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toker on 6/15/2014.
 */
public class UserCounts extends Entity {

    @SerializedName("media")
    private int mMediaCount;

    @SerializedName("follows")
    private int mFollowsCount;

    @SerializedName("followd_by")
    private int mFollowedByCount;
}
