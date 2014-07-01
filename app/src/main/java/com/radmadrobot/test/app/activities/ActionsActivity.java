package com.radmadrobot.test.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.radmadrobot.test.app.R;
import com.radmadrobot.test.app.RedmadrobotTestApp;

/**
 * Created by toker on 6/19/2014.
 */
public class ActionsActivity extends Activity {

    public void onMediaUserHasLikedClicked(View v) {
        //Toast.makeText(this, "onMediaUserHasLikedClicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(RedmadrobotTestApp.MEDIA_USER_HAS_LIKED_TAG, RedmadrobotTestApp.MEDIA_USER_HAS_LIKED_TAG);
        startActivity(intent);
    }

    public void onPopularClicked(View v) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(RedmadrobotTestApp.POPULAR_MEDIA_TAG, RedmadrobotTestApp.POPULAR_MEDIA_TAG);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}