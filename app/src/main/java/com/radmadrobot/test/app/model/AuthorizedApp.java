package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toker on 6/20/2014.
 */
public class AuthorizedApp extends Entity {

    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("user")
    private User mUser;
}
