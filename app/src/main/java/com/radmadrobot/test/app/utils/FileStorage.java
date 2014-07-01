package com.radmadrobot.test.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by toker on 6/28/2014.
 * Сохранение файлов на диск
 * */
public class FileStorage {
    private final String IMAGES_DIR_NAME = "images";

    Context mContext;

    File mImagesDir;

    ArrayList<String> mImagesPaths;

    public FileStorage(Context context) {
        mContext = context;

        mImagesDir = mContext.getDir(IMAGES_DIR_NAME, Context.MODE_PRIVATE);
        mImagesPaths = new ArrayList<String>();
    }

    public void saveImage(Bitmap bitmap) {
        String imagesDirPath = mImagesDir.getAbsolutePath();
        Log.i("Storage", "images dir path " + imagesDirPath);

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String title = n +".png";

        File file = new File(imagesDirPath, title);
        if(file.exists())
            file.delete();
        try {
            file.createNewFile();

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 75, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Storage", "file with title " + title + " saved to " + file.getAbsolutePath());
        mImagesPaths.add(file.getAbsolutePath());
    }

    public void saveImageOnExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/images");
        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String title = "Image-"+ n +".png";
        File file = new File (myDir, title);
        if (file.exists())
            file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 75, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("Storage", "file with title " + title + " saved to " + file.getAbsolutePath());
        mImagesPaths.add(file.getAbsolutePath());
    }

    public ArrayList<String> getSavedImagesPaths() {
        return mImagesPaths;
    }

    public void clearImages() {
        File[] imageFiles = mImagesDir.listFiles();

        Log.i("Storage", "files number for deleting " + imageFiles.length);

        for (File file : imageFiles) {
            Log.i("Storage", "file for deleting " + file.getName());
            boolean deleteResult = file.delete();
            Log.i("Storage", "deleting result " + deleteResult);
        }

        mImagesPaths.clear();
    }

    public void saveCheckedImages(ArrayList<Bitmap> checkedImageBitmaps) {
        for (Bitmap bitmap : checkedImageBitmaps) {
            Log.i("STORAGE", "saving bitmap " + bitmap.toString());
            saveImage(bitmap);
        }
    }

    public ArrayList<Uri> getSavedImagesUris() {
        ArrayList<Uri> res = new ArrayList<Uri>();

        for (String path : mImagesPaths) {
            File savedImageFile = new File(path);
            savedImageFile.setReadable(true, false);

            Log.i("STORAGE", "forming uri for file " + savedImageFile.getAbsolutePath());
            Uri uri = Uri.fromFile(savedImageFile);
            res.add(uri);
        }

        return res;
    }
}
