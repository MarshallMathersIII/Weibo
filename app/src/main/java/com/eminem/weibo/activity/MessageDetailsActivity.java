package com.eminem.weibo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.MessageDetailsAdapter;
import com.eminem.weibo.bean.Comment;
import com.eminem.weibo.bean.CommentsResponse;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.utils.AsyncHttpUtils;
import com.eminem.weibo.utils.TitleBuilder;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessageDetailsActivity extends BaseActivity {
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lvMessage;
    private MessageDetailsAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private int curPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        initView();
        loadData(1);
    }

    private void initView() {
        new TitleBuilder(this)
                .setTitleText("所有微博")
                .setLeftImage(R.drawable.navigationbar_back)
                .setRightText("设置")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

//        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        lvMessage = (ListView) findViewById(R.id.swipe_target);
        adapter = new MessageDetailsAdapter(this, comments);
        lvMessage.setAdapter(adapter);

//        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData(1);
//                swipeToLoadLayout.setRefreshing(false);
//
//            }
//        });
//
//        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                loadData(curPage + 1);
//                swipeToLoadLayout.setLoadingMore(false);
//            }
//        });
    }

    private void loadData(final int page) {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        String token = mAccessToken.getToken();
        RequestParams params = new RequestParams();
        params.put("page", page);
        params.put("access_token", token);
        params.put("since_id", 0);
        params.put("max_id", 0);
        params.put("count", 50);
        params.put("filr_by_author", 0);
        params.put("filter_by_source", 0);
        AsyncHttpUtils.get("comments/mentions.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Logger.json(responseString);
                if (page == 1) {
                    comments.clear();
                }
                curPage = page;
                addComments(new Gson().fromJson(responseString, CommentsResponse.class));

            }
        });


    }

    private void addComments(CommentsResponse resBean) {
        for (Comment comment : resBean.getComments()) {
            if (!comments.contains(comment)) {
                comments.add(comment);
            }
        }
        adapter.notifyDataSetChanged();

    }

}
