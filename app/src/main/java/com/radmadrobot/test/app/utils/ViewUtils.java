package com.radmadrobot.test.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by toker on 6/24/2014.
 */
public abstract class ViewUtils {

    private static Animation mFadeInAnimation;
    private static Animation mFadeOutAnimation;

    /**
     * Инициализация фэйдинг-анимации
     * @param context
     * @param duration
     */
    public static void initFadeAnimation(Context context, long duration) {
        mFadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        mFadeInAnimation.setDuration(duration);

        mFadeOutAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        mFadeOutAnimation.setDuration(duration);
    }

    /**
     * Изменение видимости вьюхи
     * @param view
     * @param visibility
     * @param animated
     */
    public static void changeViewVisibility(View view, int visibility, boolean animated) {
        if (view == null || view.getVisibility() == visibility)
            return;
        if (animated)
            view.startAnimation((visibility == View.GONE || visibility == View.INVISIBLE) ?
                    mFadeOutAnimation : mFadeInAnimation);

        view.setVisibility(visibility);
    }


}
