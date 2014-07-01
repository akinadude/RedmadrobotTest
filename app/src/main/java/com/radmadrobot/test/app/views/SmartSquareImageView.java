package com.radmadrobot.test.app.views;

import android.content.Context;
import android.util.AttributeSet;

import com.loopj.android.image.SmartImageView;

/**
 * Created by toker on 6/30/2014.
 */
public class SmartSquareImageView extends SmartImageView {
    public SmartSquareImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SmartSquareImageView(Context context) {
        super(context);
    }

    public SmartSquareImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
