package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.StatusAdapter;
import com.eminem.weibo.api.AsyncHttpUtils;
import com.eminem.weibo.bean.StatusTimeLineResponse;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import butterknife.BindView;

/**
 * Created by Eminem on 2016/11/30.
 * Home
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.titlebar_iv_left)
    ImageView titlebarIvLeft;
    @BindView(R.id.titlebar_tv_left)
    TextView titlebarTvLeft;
    @BindView(R.id.titlebar_tv)
    TextView titlebarTv;
    @BindView(R.id.titlebar_iv_right)
    ImageView titlebarIvRight;
    @BindView(R.id.titlebar_tv_right)
    TextView titlebarTvRight;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.lv_home)
    ListView lvHome;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        view = View.inflate(activity, R.layout.frag_home, null);
        new TitleBuilder(view)
                .setTitleText("首页")
                .setLeftText("LEFT")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(activity, "left onclick", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void initData() {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        String token = mAccessToken.getToken();
//      long uid = Long.parseLong(mAccessToken.getUid());
        RequestParams params = new RequestParams();
//      params.put("uid",uid);
        params.put("access_token", token);
        AsyncHttpUtils.get("statuses/home_timeline.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(activity, responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                Toast.makeText(activity, responseString, Toast.LENGTH_LONG).show();
                Log.d("eminem",responseString);
                Logger.json(responseString);
                StatusTimeLineResponse timeLineResponse = new Gson().fromJson(responseString, StatusTimeLineResponse.class);
                lvHome.setAdapter(new StatusAdapter(activity, timeLineResponse.getStatuses()));
            }
        });
    }


}
