package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toker on 6/15/2014.
 */
public class MediaFlavor {

    @SerializedName("url")
    private String mUrl;

    @SerializedName("width")
    private int mWidth;

    @SerializedName("height")
    private int mHeight;


    public String getUrl() {
        return mUrl;
    }
}
