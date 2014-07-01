package com.radmadrobot.test.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.radmadrobot.test.app.R;

import com.radmadrobot.test.app.model.Media;
import com.radmadrobot.test.app.model.MediaFlavor;
import com.loopj.android.image.SmartImageView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by toker on 6/14/2014.
 */
public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * Список из модели
     */
    private LinkedList<Media> mMediaList;

    /**
     * Список position'ов, на которых находятся чекнутые элементы
     */
    ArrayList<Integer> mCheckedViewsPositions;

    /**
     * Мапа mediaId, imageView отмеченных картинок
     */
    HashMap<String, Bitmap> mCheckedImageViewsMap;

    /**
     * Дроуабл для подсветки отмеченных элементов
     */
    Drawable mHighlightDrawable;

    /**
     * Дроуабл для отмены подсветки отмеченных элементов
     */
    Drawable mUnhighlight;


    public static class ViewHolder {
        public SmartImageView smartImageView;

        /**
         * Нужен для того, чтобы при ресайклинге вьюх в getView unchecked view
         * не отображалась как checked view
         */
        public FrameLayout highlightFrame;
    }

    public ImageGridAdapter(Context c) {
        mContext = c;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMediaList = new LinkedList<Media>();

        mCheckedViewsPositions = new ArrayList<Integer>();
        mCheckedImageViewsMap = new HashMap<String, Bitmap>();
        mHighlightDrawable = c.getResources().getDrawable(R.drawable.shape_image_highlight);
        mUnhighlight = c.getResources().getDrawable(R.drawable.shape_image_transparent);
    }

    public int getCount() {
        if (mMediaList == null)
            return 0;
        else
            return mMediaList.size();
    }

    public Object getItem(int position) {
        String mediaId = mMediaList.get(position).getId();
        return mediaId;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.i(this.getClass().getSimpleName(), "getView called");
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_gridview, null);

            holder.smartImageView = (SmartImageView) convertView.findViewById(R.id.web_cached_imageview);
            holder.highlightFrame = (FrameLayout) convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();

            Drawable imageStub = mContext.getResources().getDrawable(R.drawable.shape_image_stub);
            holder.smartImageView.setImageDrawable(imageStub);
        }

        // подсветка
        highlightView(holder.highlightFrame, isViewChecked(position));

        MediaFlavor standardResolImage = mMediaList.get(position).getImages().getStandardResolution();
        if (standardResolImage != null) {
            String imageUrl = standardResolImage.getUrl();
            holder.smartImageView.setImageUrl(imageUrl);
        }
        else {
            Log.i(this.getClass().getSimpleName(), "standard_resolution object is null");
        }

        return convertView;
    }

    /**
     * Рассказать адаптеру о списке, который он будет отображать
     * @param mediasList
     */
    public void updatePostList(LinkedList<Media> mediasList) {
        mMediaList = mediasList;

        doClearItems();

        notifyDataSetChanged();
    }

    public void clearCheckedItems() {
        doClearItems();

        notifyDataSetChanged();
    }

    private void doClearItems() {
        mCheckedViewsPositions.clear();
        mCheckedImageViewsMap.clear();
    }

    public ArrayList<Integer> getCheckedItemsPositionsArray() {
        return mCheckedViewsPositions;
    }

    public ArrayList<Bitmap> getCheckedImageViews() {
        ArrayList<Bitmap> res = new ArrayList<Bitmap>();

        for(Map.Entry<String, Bitmap> entry : mCheckedImageViewsMap.entrySet()) {
            String key = entry.getKey();
            res.add(entry.getValue());
        }

        return res;
    }

    public void highlightView(View view, boolean check) {
        //Log.i(this.getClass().getSimpleName(), "highlightView called");

        if (check) {
            //Log.i(this.getClass().getSimpleName(), "highlighting item " + position);
            ((FrameLayout) view).setForeground(mHighlightDrawable);
        }
        else {
            //Log.i(this.getClass().getSimpleName(), "unhighlighting item " + position);
            ((FrameLayout) view).setForeground(mUnhighlight);
        }
    }

    public void setViewChecked(View view, Integer position, boolean check) {
        SmartImageView smartImageView = (SmartImageView) ((FrameLayout) view).getChildAt(0);
        Bitmap bitmap = ((BitmapDrawable) smartImageView.getDrawable()).getBitmap();

        if (isViewChecked(position) && check == false) {
            mCheckedViewsPositions.remove(position);

            String key = (String)getItem(position);
            mCheckedImageViewsMap.remove(key);
        }
        else if (!isViewChecked(position) && check == true) {
            mCheckedViewsPositions.add(position);

            String key = (String)getItem(position);
            mCheckedImageViewsMap.put(key, bitmap);
        }
        highlightView(view, check);

        Log.i(this.getClass().getSimpleName(), "checked items positions arrray: " + mCheckedViewsPositions.toString());
        //Log.i(this.getClass().getSimpleName(), "checked items map: " + mCheckedImageViewsMap.toString());
    }

    public void toggle(View view, int position) {
        if (isViewChecked(position))
            setViewChecked(view, position, false);
        else
            setViewChecked(view, position, true);

        //Log.i(this.getClass().getSimpleName(), "checked items arrray" + mCheckedViewsPositions.toString());
    }

    public boolean isViewChecked(Integer viewPosition) {
        if (mCheckedViewsPositions.contains(viewPosition))
            return true;
        else
            return false;
    }
}
