package com.radmadrobot.test.app.networking;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.radmadrobot.test.app.R;
import com.radmadrobot.test.app.RedmadrobotTestApp;
import com.radmadrobot.test.app.model.Media;
import com.radmadrobot.test.app.preferences.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;


/**
 * Created by toker on 6/17/2014.
 */

/*Здесь сейчас будет очень много рефакторинга!*/

public class ServerApi {
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String COUNT_KEY = "count";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String CODE_KEY = "code";
    public static final String AUTHORIZATION_CODE_KEY = "authorization_code";

    public static final String CLIENT_ID = "69ed58ff5baa42eab2e0c971b230e35e";
    public static final String CLIENT_SECRET = "adba0fbe63c1441d8d372ed0fb2d190b";

    public static final String MEDIA_POPULAR = "media/popular";
    public static final String MEDIA_USER_HAS_LIKED = "users/self/media/liked";

    public static final String AUTH_URL = "https://instagram.com/oauth/authorize/";
    public static final String ACCESS_TOKEN_URL = "https://api.instagrm.comoauth/access_token";
    public static final String REDIRECT_URL = "http://example.com";

    /**
     * Енум типов реквестов
     */
    private enum HttpMethod {GET, POST, PUT, DELETE};

    /**
     * Реквест
     */
    private static class Request {
        public HttpMethod mHttpMethod;
        public String mPartialUrl;
        public HashMap<String, String> mUrlParams; // присутствует у всех
        public HashMap<String, String> mBodyParams; // присутствует только у POST

        public Request(HttpMethod method, String partialUrl, HashMap<String, String> urlParams) {
            this(method, partialUrl, urlParams, new HashMap<String, String>());
        }

        public Request(HttpMethod method, String partialUrl, HashMap<String, String> urlParams,
                       HashMap<String, String> bodyParams) {
            mHttpMethod = method;
            mPartialUrl = partialUrl;
            mUrlParams = urlParams;
            mBodyParams = bodyParams;
        }
    }

    /**
     * Респонс
     */
    private static class Response {
        public int mCode;
        public String mMessage;
        public String mJsonString;

        public Response() {}

        public Response(int code, String message, String jsonString) {
            mCode = code;
            mMessage = message;
            mJsonString = jsonString;
        }
    }

    private Context mContext;

    private String mBaseUrlString;

    private int mConnectionTimeout;

    private int mReadTimeout;

    private HttpURLConnection mConnection;

    private Preferences mPreferences;


    /**
     * Конструктор
     * @param context
     */
    public ServerApi(Context context) {
        mContext = context;

        mPreferences = RedmadrobotTestApp.getInstance().getPreferences();

        /*mBaseUrlString = mContext.getString(R.string.base_test_url);*/
        mBaseUrlString = mContext.getString(R.string.base_url);
        mConnectionTimeout = Integer.parseInt(mContext.getString(R.string.connection_timeout));
        mReadTimeout = Integer.parseInt(mContext.getString(R.string.read_timeout));
    }

    /**
     * Ваозвращает url-параметры форматированной строкой
     * @param params
     * @return
     */
    private String urlParamsToString(HashMap<String, String> params) {
        Set<String> keys = params.keySet();
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (String key : keys) {
            if (first) {
                builder.append("?");
                first = false;
            }
            else
                builder.append("&");

            try {
                builder.append(URLEncoder.encode(key, "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    /**
     * Ваозвращает body-параметры форматированной строкой
     * @param params
     * @return
     */
    private String bodyParamsToString(HashMap<String, String> params) {
        Set<String> keys = params.keySet();
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (String key : keys) {
            if (first) {
                first = false;
            }
            else
                builder.append("&");

            try {
                builder.append(URLEncoder.encode(key, "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    /**
     * Выполнить реквест, вернуть респонс
     * @param request
     * @return Респонс
     * @throws JSONException
     * @throws IOException
     */
    private Response execute(Request request) throws JSONException, IOException {
        Response res;

        String fullUrlString = null;
        URL url = null;
        String bodyParamsString = null;

        // формируем полный url с параметрами
        fullUrlString = mBaseUrlString + request.mPartialUrl;
        if (!request.mUrlParams.isEmpty()) {
            String urlParamsString = urlParamsToString(request.mUrlParams);
            fullUrlString += urlParamsString;
        }
        url = new URL(fullUrlString);

        if (request.mHttpMethod == HttpMethod.POST) {
            if (!request.mBodyParams.isEmpty()) {
                bodyParamsString = bodyParamsToString(request.mBodyParams);
            }
        }

        mConnection = (HttpURLConnection) url.openConnection();
        mConnection.setRequestMethod(request.mHttpMethod.toString());
        mConnection.setReadTimeout(mReadTimeout);
        mConnection.setConnectTimeout(mConnectionTimeout);

        if (request.mHttpMethod == HttpMethod.POST) {
            mConnection.setDoInput(true);
            mConnection.setDoOutput(true);
            NetworkUtils.stringToStream(bodyParamsString, mConnection.getOutputStream(), true);
        }

        // отправка реквеста и получение респонса
        String jsonString = NetworkUtils.streamToString(mConnection.getInputStream());

        int responseCode = mConnection.getResponseCode();
        String responseMessage = mConnection.getResponseMessage();

        res = new Response(responseCode, responseMessage, jsonString);
        mConnection.disconnect();

        return res;
    }


    /**
     * Вытянуть популярное
     * @return
     */
    public LinkedList<Media> getPopular() {
        HashMap<String, String> urlParams = new HashMap<String, String>();
        urlParams.put(CLIENT_ID_KEY, CLIENT_ID);
        urlParams.put(COUNT_KEY, "21");

        Request request = new Request(HttpMethod.GET, MEDIA_POPULAR, urlParams);
        Response response = new Response();

        LinkedList<Media> res = new LinkedList<Media>();

        try {
            response = execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // парсинг результата
        try {
            JSONObject rootObj = new JSONObject(response.mJsonString);
            JSONArray mediasArray = rootObj.getJSONArray("data");
            String mediasArrayString = mediasArray.toString();

            res = new Gson().fromJson(mediasArrayString, (new TypeToken<LinkedList<Media>>(){}).getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public LinkedList<Media> getMediaUserHasLiked() {
        HashMap<String, String> urlParams = new HashMap<String, String>();

        String accessToken = mPreferences.getAccessToken();
        Log.i(this.getClass().getSimpleName(), "assess_token=" + accessToken);

        urlParams.put(ACCESS_TOKEN_KEY, accessToken);
        urlParams.put(COUNT_KEY, "21");

        Request request = new Request(HttpMethod.GET, MEDIA_USER_HAS_LIKED, urlParams);
        Response response = new Response();

        LinkedList<Media> res = new LinkedList<Media>();

        try {
            response = execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // парсинг результата
        try {
            JSONObject rootObj = new JSONObject(response.mJsonString);
            JSONArray mediasArray = rootObj.getJSONArray("data");
            String mediasArrayString = mediasArray.toString();

            res = new Gson().fromJson(mediasArrayString, (new TypeToken<LinkedList<Media>>(){}).getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Тестовый POST-реквест
     * @return
     */
    public String testPostRequest() {
        HashMap<String, String> urlParams = new HashMap<String, String>();
        urlParams.put(CLIENT_ID_KEY, CLIENT_ID);
        urlParams.put(CLIENT_SECRET_KEY, CLIENT_SECRET);

        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("body_param1", "body_value1");
        bodyParams.put("body_param2", "body_value2");

        Request request = new Request(HttpMethod.POST, "", urlParams);
        Response response = new Response();

        try {
            response = execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.mJsonString;
    }
}