package com.radmadrobot.test.app.model;

import com.google.gson.Gson;

/**
 * Created by toker on 6/15/2014.
 */
public class Entity {

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
