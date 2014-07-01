package com.radmadrobot.test.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.radmadrobot.test.app.adapters.ImageGridAdapter;
import com.radmadrobot.test.app.R;
import com.radmadrobot.test.app.RedmadrobotTestApp;
import com.radmadrobot.test.app.model.Media;

import com.radmadrobot.test.app.networking.ServerApi;
import com.radmadrobot.test.app.preferences.Preferences;
import com.radmadrobot.test.app.utils.FileStorage;
import com.radmadrobot.test.app.utils.ViewUtils;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Активити для отображения грида медиа-тумбнейлов
 */
public class ImageGridActivity extends ActionBarActivity {

    /**
     * Контекст активити
     */
    private Context mContext;

    /**
     * Ссылка на серверное API
     */
    private ServerApi mServerApi;

    /**
     * Ссылка на преференсы
     */
    private Preferences mPreferences;

    private FileStorage mFileStorage;

    /**
     * Адаптер грида
     */
    private ImageGridAdapter mImageGridAdapter;

    /**
     * Список медиа (фотки | видео)
     */
    private LinkedList<Media> mMediaList;

    /**
     * грид фоток
     */
    private GridView mPhotosGridView;

    /**
     * Прогрессбар
     */
    private ProgressBar mProgressbar;

    private MenuItem mReloadItem;

    private MenuItem mSendEmailItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mServerApi = RedmadrobotTestApp.getInstance().getServerApi();
        mPreferences = RedmadrobotTestApp.getInstance().getPreferences();
        mFileStorage = RedmadrobotTestApp.getInstance().getFileStorage();

        mImageGridAdapter = new ImageGridAdapter(this);

        mPhotosGridView = (GridView) findViewById(R.id.photos_gridview);
        mPhotosGridView.setAdapter(mImageGridAdapter);
        mPhotosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageGridAdapter adapter = (ImageGridAdapter) parent.getAdapter();
                adapter.toggle(view, position);

                Log.i("DEBUG_TAG", "checked image views number: " + adapter.getCheckedImageViews().size());
                Log.i("DEBUG_TAG", "checked positions number: " + adapter.getCheckedItemsPositionsArray().size());
            }
        });
        mProgressbar = (ProgressBar) findViewById(R.id.photos_progressbar);

        Intent intent = getIntent();
        if (intent.hasExtra(RedmadrobotTestApp.MEDIA_USER_HAS_LIKED_TAG)) {
            new GetMediaUserHasLikedTask().execute();
        }
        else if (intent.hasExtra(RedmadrobotTestApp.POPULAR_MEDIA_TAG)) {
            new GetPopularTask().execute();
        }

        mFileStorage.clearImages();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_image_grid, menu);

        if (getIntent().hasExtra(RedmadrobotTestApp.MEDIA_USER_HAS_LIKED_TAG)) {
            MenuItem actionUpdateItem = menu.findItem(R.id.action_update);
            actionUpdateItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_send_email:
                sendEmailWithImagesAttached();
                mSendEmailItem = item;
                mSendEmailItem.setEnabled(false);

                return true;
            case R.id.action_update:
                Intent intent = getIntent();
                if (intent.hasExtra(RedmadrobotTestApp.MEDIA_USER_HAS_LIKED_TAG)) {
                    new GetMediaUserHasLikedTask().execute();
                }
                else if (intent.hasExtra(RedmadrobotTestApp.POPULAR_MEDIA_TAG)) {
                    new GetPopularTask().execute();
                }

                mFileStorage.clearImages();

                mReloadItem = item;
                mReloadItem.setEnabled(false);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendEmailWithImagesAttached() {
        new SendEmailTask().execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivityResult", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        /*if (requestCode == RedmadrobotTestApp.SEND_EMAIL_RERQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(this, "email was successfully sent", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this, "email sending was cancelled", Toast.LENGTH_SHORT).show();
            }
        }*/

        mFileStorage.clearImages();
        mImageGridAdapter.clearCheckedItems();
    }


    private void startEmailActivityForResult() {
        ArrayList<Uri> imagesUris = mFileStorage.getSavedImagesUris();

        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"toker-rg@yandex.ru"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject");
        i.putExtra(Intent.EXTRA_TEXT, "text");
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imagesUris);
        try {
            int sendEmailRequestCode = getResources().getInteger(R.integer.send_email_request_code);
            startActivityForResult(Intent.createChooser(i, "Send email..."),sendEmailRequestCode);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        } catch (ClassCastException e) {
            Toast.makeText(mContext, "Class cast exception", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Таск для вытягивание популярных медиа
     */
    public class GetPopularTask extends AsyncTask<Void, Void, LinkedList<Media>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ViewUtils.changeViewVisibility(mProgressbar, View.VISIBLE, true);
            ViewUtils.changeViewVisibility(mPhotosGridView, View.INVISIBLE, true);
        }

        @Override
        protected LinkedList<Media> doInBackground(Void... params) {
            //LinkedList<Media> result;

            mMediaList = mServerApi.getPopular();

            return mMediaList;
        }

        @Override
        protected void onPostExecute(LinkedList<Media> result) {
            super.onPostExecute(result);

            mImageGridAdapter.updatePostList(result);
            if (mReloadItem != null)
                mReloadItem.setEnabled(true);

            ViewUtils.changeViewVisibility(mProgressbar, View.GONE, true);
            ViewUtils.changeViewVisibility(mPhotosGridView, View.VISIBLE, true);
        }
    }


    public class GetMediaUserHasLikedTask extends AsyncTask<Void, Void, LinkedList<Media>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ViewUtils.changeViewVisibility(mProgressbar, View.VISIBLE, true);
            ViewUtils.changeViewVisibility(mPhotosGridView, View.INVISIBLE, true);
        }

        @Override
        protected LinkedList<Media> doInBackground(Void... params) {
            mMediaList = mServerApi.getMediaUserHasLiked();
            return mMediaList;
        }

        @Override
        protected void onPostExecute(LinkedList<Media> result) {
            super.onPostExecute(result);

            mImageGridAdapter.updatePostList(result);
            if (mReloadItem != null)
                mReloadItem.setEnabled(true);

            ViewUtils.changeViewVisibility(mProgressbar, View.GONE, true);
            ViewUtils.changeViewVisibility(mPhotosGridView, View.VISIBLE, true);
        }
    }


    /**
     * Таск посылки письмеца с аттачед фотками
     */
    public class SendEmailTask extends AsyncTask<Void, Void, Void> {
        ArrayList<Bitmap> mCheckedImageBitmaps;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ViewUtils.changeViewVisibility(mProgressbar, View.VISIBLE, true);
            ViewUtils.changeViewVisibility(mPhotosGridView, View.INVISIBLE, true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mCheckedImageBitmaps = mImageGridAdapter.getCheckedImageViews();
            Log.i("STORAGE", "checked image views number" + mCheckedImageBitmaps.size());
            mFileStorage.saveCheckedImages(mCheckedImageBitmaps);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            startEmailActivityForResult();

            mSendEmailItem.setEnabled(true);

            ViewUtils.changeViewVisibility(mProgressbar, View.GONE, true);
            ViewUtils.changeViewVisibility(mPhotosGridView, View.VISIBLE, true);
        }
    }
}