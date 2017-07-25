package com.eminem.weibo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.constants.WeiboConstants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class MagicLoginActivity extends BaseActivity {
    private String sRedirectUri;
    private WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_login);
        String url = getIntent().getStringExtra("url");
        sRedirectUri = WeiboConstants.REDIRECT_URL;
        mWeb = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWeb.setWebViewClient(new MyWebViewClient());
        mWeb.loadUrl(url);
        mWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                        if (mWeb.canGoBack()) {
                            mWeb.goBack();
                        } else {
//                            Intent intent = new Intent(MagicLoginActivity.this, UnLoginActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(url);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }


    public boolean isUrlRedirected(String url) {
        return url.startsWith(sRedirectUri);
    }

    private void handleRedirectedUrl(String url) {
        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token=");
            int expiresIndex = url.indexOf("expires_in=");
            int refresh_token_Index = url.indexOf("refresh_token=");
            int uid_Index = url.indexOf("uid=");
            int scope_Index = url.indexOf("scope=");

            String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));
            String refresh_token = url.substring(refresh_token_Index + 14, url.indexOf("&", refresh_token_Index));
            String uid = new String();
            if (url.contains("scope=")) {
                uid = url.substring(uid_Index + 4, url.indexOf("&", uid_Index));
            } else {
                uid = url.substring(uid_Index + 4);
            }


//            LogUtil.d("url = " + url);
//            LogUtil.d("token = " + token);
//            LogUtil.d("expires_in = " + expiresIn);
//            LogUtil.d("refresh_token = " + refresh_token);
//            LogUtil.d("uid = " + uid);

            Oauth2AccessToken mAccessToken = new Oauth2AccessToken();
            mAccessToken.setToken(token);
            mAccessToken.setExpiresIn(expiresIn);
            mAccessToken.setRefreshToken(refresh_token);
            mAccessToken.setUid(uid);
            AccessTokenKeeper.writeAccessToken(MagicLoginActivity.this, mAccessToken);
            Intent intent = new Intent(MagicLoginActivity.this, MainActivity.class);
            intent.putExtra("fisrtstart", true);
            startActivity(intent);
            finish();

        }
    }
}
