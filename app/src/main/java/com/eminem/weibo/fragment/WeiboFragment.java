package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.StatusAdapter;
import com.eminem.weibo.utils.AsyncHttpUtils;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.StatusTimeLineResponse;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Eminem on 2016/11/30.
 * Home
 */

public class WeiboFragment extends BaseFragment {
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lvHome;
    private View view;
    private StatusAdapter adapter;
    private List<Status> statuses = new ArrayList<>();
    private int curPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initData(1);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        view = View.inflate(activity, R.layout.frag_weibo, null);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        lvHome = (ListView) view.findViewById(R.id.swipe_target);
        new TitleBuilder(view)
                .setTitleText("首页")
                .setLeftText("LEFT")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(activity, "left onclick", Toast.LENGTH_SHORT);
                    }
                });


        adapter = new StatusAdapter(activity, statuses);
        lvHome.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1);
                swipeToLoadLayout.setRefreshing(false);

            }
        });

        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData(curPage + 1);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

    }

    private void initData(final int page) {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        String token = mAccessToken.getToken();
        RequestParams params = new RequestParams();
        params.put("page", page);
        params.put("access_token", token);
        AsyncHttpUtils.get("statuses/home_timeline.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(activity, responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Logger.json(responseString);
                StatusTimeLineResponse timeLineResponse = new Gson().fromJson(responseString, StatusTimeLineResponse.class);
//              lvHome.setAdapter(new StatusAdapter(activity, timeLineResponse.getStatuses()));

                if (page == 1) {
                    statuses.clear();
                }
                curPage = page;
//              lvHome.setAdapter(new StatusAdapter(activity, timeLineResponse.getStatuses()));
                addStatus(new Gson().fromJson(responseString, StatusTimeLineResponse.class));

            }
        });
    }


    private void addStatus(StatusTimeLineResponse resBean) {
        for (Status status : resBean.getStatuses()) {
            if (!statuses.contains(status)) {
                statuses.add(status);
            }
        }
        adapter.notifyDataSetChanged();

    }


}
