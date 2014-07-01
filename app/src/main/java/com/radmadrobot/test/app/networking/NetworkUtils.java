package com.radmadrobot.test.app.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * Created by toker on 6/22/2014.
 */
public abstract class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                                                                    Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * Отправляем параметры, находащиеся в теле запроса, в поток
     * @param output
     * @param stream
     * @param shouldCloseStream
     */
    public static void stringToStream(String output, OutputStream stream,
                                        boolean shouldCloseStream) {
        try {
            stream.write(output.getBytes());

            if (shouldCloseStream) {
                stream.flush();
                stream.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Получить тело респонса строкой
     * @param inputStream
     * @return
     */
    public static String streamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String buf = "";

        try {
            while ((buf = reader.readLine()) != null) {
                total.append(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total.toString();
    }
}
