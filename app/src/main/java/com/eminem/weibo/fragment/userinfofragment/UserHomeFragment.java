package com.eminem.weibo.fragment.userinfofragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eminem.weibo.R;
import com.eminem.weibo.adapter.StatusAdapter;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.StatusTimeLineResponse;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.utils.AsyncHttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by eminem on 2017/7/20.
 */

public class UserHomeFragment extends Fragment {
    public static final String TAG = "UserHomeFragment";

    private ListView LvUserHome;
    private View view;
    private StatusAdapter adapter;
    private List<Status> statuses = new ArrayList<>();
    private int curPage = 1;

    private String screenName;//screen_name需要查询的用户昵称

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        screenName = getActivity().getIntent().getStringExtra("screen_name");
        Log.d(TAG, screenName);
        initList();
        loadStatuses(1);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initList() {
        view = View.inflate(getContext(), R.layout.frag_user_home, null);
        LvUserHome = (ListView) view.findViewById(R.id.lv_user_home);
//        LvUserHome.setNestedScrollingEnabled(true);
        adapter = new StatusAdapter(getContext(), statuses);
        LvUserHome.setAdapter(adapter);
    }

    private void loadStatuses(final int page) {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
        String token = mAccessToken.getToken();
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("screen_name", screenName);
        //此接口最多只返回最新的5条数据，官方移动SDK调用可返回10条；
        AsyncHttpUtils.get("statuses/user_timeline.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (page == 1) {
                    statuses.clear();
                }
                curPage = page;

                addStatus(new Gson().fromJson(responseString, StatusTimeLineResponse.class));
            }
        });

    }

    private void addStatus(StatusTimeLineResponse statusTimeLineResponse) {
        for (Status status : statusTimeLineResponse.getStatuses()) {
            if (!statuses.contains(status)) {
                statuses.add(status);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
