package com.radmadrobot.test.app;

import android.app.Application;
import android.content.Context;

import com.radmadrobot.test.app.networking.ServerApi;
import com.radmadrobot.test.app.preferences.Preferences;
import com.radmadrobot.test.app.utils.FileStorage;
import com.radmadrobot.test.app.utils.ViewUtils;

/**
 * Created by toker on 6/16/2014.
 */
public class RedmadrobotTestApp extends Application {
    public static final String ACCESS_TOKEN_TAG = "access_token_tag";
    public static final String AUTH_FAILED_TAG = "auth_failed_tag";
    public static final String POPULAR_MEDIA_TAG = "popular_media_tag";
    public static final String MEDIA_USER_HAS_LIKED_TAG = "media_user_has_liked_tag";

    private Preferences mPreferences;

    private ServerApi mServerApi;

    private FileStorage mFileStorage;

    private static RedmadrobotTestApp sInstance;

    public static synchronized RedmadrobotTestApp getInstance() {
        return sInstance;
    }

    private void initInstance() {
        mPreferences = new Preferences(this);
        mServerApi = new ServerApi(this);
        mFileStorage = new FileStorage(this);

        int fade_anim_duration = getResources().getInteger(R.integer.fade_anim_duration_ms);
        ViewUtils.initFadeAnimation(this, fade_anim_duration);
    }

    public ServerApi getServerApi() {
        return mServerApi;
    }

    public Preferences getPreferences() {
        return mPreferences;
    }

    public FileStorage getFileStorage() {
        return mFileStorage;
    }

    public static boolean isOldAndroid() {
        return (android.os.Build.VERSION.SDK_INT <
                android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sInstance.initInstance();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}