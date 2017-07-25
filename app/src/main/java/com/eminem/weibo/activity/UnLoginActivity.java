package com.eminem.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.constants.WeiboConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnLoginActivity extends BaseActivity {


    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + WeiboConstants.APP_KEY
                + "&response_type=token&redirect_uri=" + WeiboConstants.REDIRECT_URL
                + "&key_hash=" + WeiboConstants.AppSecret + (TextUtils.isEmpty(WeiboConstants.PackageName) ? "" : "&packagename=" + WeiboConstants.PackageName)
                + "&display=mobile" + "&scope=" + WeiboConstants.SCOPE;

        Intent intent = new Intent(this, MagicLoginActivity.class);
        intent.putExtra("url", authurl);
        startActivity(intent);
        finish();
    }
}
