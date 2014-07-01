package com.radmadrobot.test.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.radmadrobot.test.app.RedmadrobotTestApp;
import com.radmadrobot.test.app.preferences.Preferences;

/**
 * Created by toker on 6/24/2014.
 */
public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences prefs = RedmadrobotTestApp.getInstance().getPreferences();

        String accessToken = prefs.getAccessToken();
        Log.i(this.getClass().getSimpleName(),
                "access_token = " + (accessToken == null ? "null" : accessToken));

        if (accessToken == null) {
            Intent intent = new Intent(this, NeedAuthAppActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, ActionsActivity.class);
            startActivity(intent);
        }
    }
}
