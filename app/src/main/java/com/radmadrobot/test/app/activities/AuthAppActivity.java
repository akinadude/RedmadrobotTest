package com.radmadrobot.test.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.radmadrobot.test.app.R;
import com.radmadrobot.test.app.RedmadrobotTestApp;
import com.radmadrobot.test.app.networking.ServerApi;
import com.radmadrobot.test.app.preferences.Preferences;
import com.radmadrobot.test.app.utils.ViewUtils;

/**
 * Created by toker on 6/20/2014.
 */
public class AuthAppActivity extends Activity {
    /**
     * Контекст активити
     */
    private Context mContext;

    private Preferences mPreferences;

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private String mAuthRequestUrl;

    public static final String AUTH_CANCELLED_MESSAGE = "You've cancelled app authorization";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_app);

        mContext = this;
        mPreferences = RedmadrobotTestApp.getInstance().getPreferences();

        mAuthRequestUrl = ServerApi.AUTH_URL + "?" +
                            ServerApi.CLIENT_ID_KEY + "=" + ServerApi.CLIENT_ID +
                            "&redirect_uri=" + ServerApi.REDIRECT_URL +
                            "&scope=likes+comments"+
                            "&response_type=token";

        mWebView = (WebView) findViewById(R.id.auth_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new AuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mAuthRequestUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Bundle bundle = new Bundle();
        bundle.putString(RedmadrobotTestApp.AUTH_FAILED_TAG, AUTH_CANCELLED_MESSAGE);
        goToNeedAuthAppActivity(bundle);
    }

    private void goToActionsActivity() {
        Intent intent = new Intent(this, ActionsActivity.class);
        startActivity(intent);

        finish();
    }

    private void goToNeedAuthAppActivity(Bundle bundle) {
        Intent intent = new Intent(this, NeedAuthAppActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        finish();
    }

    /**
     * Класс для обработки редиректа
     */
    public class AuthWebViewClient extends WebViewClient {
        String accessToken = null;
        String errorDescription = null;
        Intent resultIntent = null;

        public void onPageStarted (WebView view, String url, Bitmap favicon) {
            Log.i(this.getClass().getSimpleName(), "page started, url=" + url);

           /* ViewUtils.changeViewVisibility(mWebView, View.GONE, true);
            ViewUtils.changeViewVisibility(mProgressBar, View.VISIBLE, true);*/

            //Toast.makeText(AuthAppActivity.this, "content loading started", Toast.LENGTH_SHORT).show();
        }
        public void onPageFinished(WebView view, String url) {
            Log.i(this.getClass().getSimpleName(), "page finished, url=" + url);

            /*ViewUtils.changeViewVisibility(mWebView, View.VISIBLE, true);
            ViewUtils.changeViewVisibility(mProgressBar, View.GONE, true);*/

            //Toast.makeText(AuthAppActivity.this, "content loading finished", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.startsWith(ServerApi.REDIRECT_URL)) {
                Log.i(this.getClass().getSimpleName(), "redirected url: " + url);
                resultIntent = new Intent();

                if (url.contains("access_token")) {
                    String parts[] = url.split("=");
                    accessToken = parts[1];

                    mPreferences.putAccessToken(accessToken);
                    goToActionsActivity();
                }
                else {
                    String splittedUrl[] = url.split("\\?");
                    String[] params = splittedUrl[1].split("&");
                    String[] splittedErrorDescription = params[2].split("=");
                    errorDescription = splittedErrorDescription[1];

                    Bundle bundle = new Bundle();
                    bundle.putString(RedmadrobotTestApp.AUTH_FAILED_TAG, AUTH_CANCELLED_MESSAGE);
                    goToNeedAuthAppActivity(bundle);
                }

                return true;
            }
            return false;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i(this.getClass().getSimpleName(), "authentication failed, description: "
                    + description + ", errorCode = " + errorCode);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(RedmadrobotTestApp.AUTH_FAILED_TAG, description);
            setResult(Activity.RESULT_CANCELED, resultIntent);
            AuthAppActivity.this.finish();
        }
    }
}