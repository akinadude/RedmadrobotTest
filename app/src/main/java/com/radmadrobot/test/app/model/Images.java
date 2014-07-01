package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by toker on 6/15/2014.
 */
public class Images {

    @SerializedName("low_resolution")
    private MediaFlavor mLowResolution;

    @SerializedName("thumbnail")
    private MediaFlavor mThumbnail;

    @SerializedName("standard_resolution")
    private MediaFlavor mStandardResolution;


    public MediaFlavor getLowResolution() {
        return mLowResolution;
    }

    public MediaFlavor getThumbnail() {
        return mThumbnail;
    }

    public MediaFlavor getStandardResolution() {
        return mStandardResolution;
    }
}
