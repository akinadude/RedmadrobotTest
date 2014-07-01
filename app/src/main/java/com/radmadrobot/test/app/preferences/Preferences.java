package com.radmadrobot.test.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by toker on 6/17/2014.
 */
public class Preferences {

    private static final String PREFERENCES_NAME = "application_preferences";

    private static final String ACCESS_TOKEN = "access_token";

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    public Preferences(Context context) {
        init(context);
    }

    private void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public Preferences putAccessToken(String token) {
        mEditor.putString(ACCESS_TOKEN, token);
        mEditor.commit();
        return this;
    }

    public String getAccessToken() {
        String res = mSharedPreferences.getString(ACCESS_TOKEN, null);
        return res;
    }
}
