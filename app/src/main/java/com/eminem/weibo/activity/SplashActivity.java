package com.eminem.weibo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class SplashActivity extends BaseActivity {
    private Oauth2AccessToken accessToken;
    private static final int WHAT_INTENT2LOGIN = 1;
    private static final int WHAT_INTENT2MAIN = 2;
    private static final long SPLASH_DUR_TIME = 1000;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_INTENT2MAIN:
                    intent2Activity(MainActivity.class);
                    finish();
                    break;
                case WHAT_INTENT2LOGIN:
                    intent2Activity(LoginActivity.class);
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();

        if(Intent.ACTION_VIEW.equals(action)){
            Uri uri = i_getvalue.getData();
            if(uri != null){
                String name = uri.getQueryParameter("name");
                String age= uri.getQueryParameter("age");
            }
        }
        accessToken = AccessTokenKeeper.readAccessToken(this);
        if (accessToken.isSessionValid()) {
            handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
        } else {
            handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
        }


    }
}
