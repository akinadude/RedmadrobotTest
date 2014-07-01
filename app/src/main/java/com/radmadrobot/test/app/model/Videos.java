package com.radmadrobot.test.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by toker on 6/15/2014.
 */
public class Videos {

    @SerializedName("videos")
    private LinkedList<MediaFlavor> mMediaFlavors;
}