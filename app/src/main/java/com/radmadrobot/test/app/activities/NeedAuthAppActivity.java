package com.radmadrobot.test.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.radmadrobot.test.app.R;
import com.radmadrobot.test.app.RedmadrobotTestApp;
import com.radmadrobot.test.app.networking.ServerApi;
import com.radmadrobot.test.app.preferences.Preferences;

/**
 * Created by toker on 6/18/2014.
 */
public class NeedAuthAppActivity extends Activity {

    public void onAuthAppClicked(View v) {
        Intent intent = new Intent(this, AuthAppActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_auth_app);

        Log.i(this.getClass().getSimpleName(), "onCreate called");

        // проверяем откуда пришли в эту активити
        Intent intent = getIntent();
        String message = intent.getStringExtra(RedmadrobotTestApp.AUTH_FAILED_TAG);
        Log.i(this.getClass().getSimpleName(), "message=" + message);
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}