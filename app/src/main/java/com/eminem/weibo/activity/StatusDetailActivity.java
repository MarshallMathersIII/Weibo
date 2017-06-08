package com.eminem.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.StatusCommentAdapter;
import com.eminem.weibo.adapter.StatusGridImgsAdapter;
import com.eminem.weibo.bean.Comment;
import com.eminem.weibo.bean.CommentsResponse;
import com.eminem.weibo.bean.PicUrls;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.constants.WeiboConstants;
import com.eminem.weibo.utils.AsyncHttpUtils;
import com.eminem.weibo.utils.DateUtils;
import com.eminem.weibo.utils.StringUtils;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;
import com.eminem.weibo.widget.WrapHeightGridView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.CommentsAPI;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class StatusDetailActivity extends BaseActivity implements View.OnClickListener {
    private View status_detail_info;
    private ImageView iv_head;
    private TextView tv_head_name;
    private TextView tv_head_desc;
    private FrameLayout include_status_image;
    private WrapHeightGridView gv_images;
    private ImageView iv_image;
    private TextView tv_content;
    private View include_retweeted_status;
    private TextView tv_retweeted_content;
    private FrameLayout fl_retweeted_imageview;
    private GridView gv_retweeted_images;
    private ImageView iv_retweeted_image;


    // shadow_tab - 顶部悬浮的菜单栏
    private View shadow_status_detail_tab;
    private RadioGroup shadow_rg_status_detail;
    private RadioButton shadow_rb_retweets;
    private RadioButton shadow_rb_comments;
    private RadioButton shadow_rb_likes;

    private View status_detail_tab;
    private RadioGroup rg_status_detail;
    private RadioButton rb_retweets;
    private RadioButton rb_comments;
    private RadioButton rb_likes;

    private LinearLayout ll_bottom_control;
    private LinearLayout ll_share_bottom;
    private TextView tv_share_bottom;
    private LinearLayout ll_comment_bottom;
    private TextView tv_comment_bottom;
    private LinearLayout ll_like_bottom;
    private TextView tv_like_bottom;

    private ListView lv_status_detail;
    private StatusCommentAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private Status status;
    private Oauth2AccessToken mAccessToken;
    private CommentsAPI mCommentsAPI;
    private int curPage = 1;
    private SwipeToLoadLayout swipeToLoadLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentsAPI = new CommentsAPI(this, WeiboConstants.APP_KEY, mAccessToken);
        status = (Status) getIntent().getSerializableExtra("status");
        initView();
        initData();
        loadComments(1);
    }

    private void initView() {
        initTitle();
        initDetailHead();
        initTab();
        initListView();
        initBottomBar();
    }

    private void initTitle() {
        new TitleBuilder(this)
                .setTitleText("微博正文")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StatusDetailActivity.this.finish();
                    }
                });
    }

    private void initDetailHead() {
        status_detail_info = View.inflate(this, R.layout.item_status, null);
        status_detail_info.setBackgroundResource(R.color.white);
        status_detail_info.findViewById(R.id.ll_bottom_control).setVisibility(View.GONE);
        iv_head = (ImageView) status_detail_info.findViewById(R.id.iv_head);
        tv_head_name = (TextView) status_detail_info.findViewById(R.id.tv_head_name);
        tv_head_desc = (TextView) status_detail_info.findViewById(R.id.tv_head_desc);
        include_status_image = (FrameLayout) status_detail_info.findViewById(R.id.include_status_image);
        gv_images = (WrapHeightGridView) status_detail_info.findViewById(R.id.gv_images);
        iv_image = (ImageView) status_detail_info.findViewById(R.id.iv_image);
        tv_content = (TextView) status_detail_info.findViewById(R.id.tv_content);
        include_retweeted_status = status_detail_info.findViewById(R.id.include_retweeted_status);
        tv_retweeted_content = (TextView) status_detail_info.findViewById(R.id.tv_retweeted_content);
        fl_retweeted_imageview = (FrameLayout) include_retweeted_status.findViewById(R.id.include_status_image);
        gv_retweeted_images = (GridView) fl_retweeted_imageview.findViewById(R.id.gv_images);
        iv_retweeted_image = (ImageView) fl_retweeted_imageview.findViewById(R.id.iv_image);
    }

    private void initTab() {
        // shadow
        shadow_status_detail_tab = findViewById(R.id.status_detail_tab);
        shadow_rg_status_detail = (RadioGroup) shadow_status_detail_tab.findViewById(R.id.rg_status_detail);
        shadow_rb_retweets = (RadioButton) shadow_status_detail_tab.findViewById(R.id.rb_retweets);
        shadow_rb_comments = (RadioButton) shadow_status_detail_tab.findViewById(R.id.rb_comments);
        shadow_rb_likes = (RadioButton) shadow_status_detail_tab.findViewById(R.id.rb_likes);

        status_detail_tab = View.inflate(this, R.layout.status_detial_tab, null);
        rg_status_detail = (RadioGroup) status_detail_tab.findViewById(R.id.rg_status_detail);
        rb_retweets = (RadioButton) status_detail_tab.findViewById(R.id.rb_retweets);
        rb_comments = (RadioButton) status_detail_tab.findViewById(R.id.rb_comments);
        rb_likes = (RadioButton) status_detail_tab.findViewById(R.id.rb_likes);

    }

    private void initListView() {
        lv_status_detail = (ListView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        adapter = new StatusCommentAdapter(this, comments);
        lv_status_detail.addHeaderView(status_detail_info);
        lv_status_detail.addHeaderView(status_detail_tab);
        lv_status_detail.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadComments(1);
                swipeToLoadLayout.setRefreshing(false);

            }
        });

        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadComments(curPage + 1);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

        lv_status_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                shadow_status_detail_tab.setVisibility(firstVisibleItem >= 2 ? View.VISIBLE : View.GONE);
            }
        });


    }

    private void initBottomBar() {
        ll_bottom_control = (LinearLayout) findViewById(R.id.status_detail_controlbar);
        ll_share_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_share_bottom);
        tv_share_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_weibo_bottom_retweet);
        ll_comment_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_comment_bottom);
        tv_comment_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_weibo_bottom_comment);
        ll_like_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_like_bottom);
        tv_like_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_weibo_bottom_like);
        ll_bottom_control.setBackgroundResource(R.color.white);

        ll_share_bottom.setOnClickListener(this);
        ll_comment_bottom.setOnClickListener(this);
        ll_like_bottom.setOnClickListener(this);

    }

    private void initData() {
        User user = status.getUser();
        Glide.with(this).load(user.getProfile_image_url()).into(iv_head);
        tv_head_name.setText(user.getName());
        tv_head_desc.setText(DateUtils.getShortTime(status.getCreated_at()) + " 来自" + Html.fromHtml(status.getSource()));

        setImages(status, include_status_image, gv_images, iv_image);

        if (TextUtils.isEmpty(status.getText())) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setVisibility(View.VISIBLE);
            SpannableString weiboContent = StringUtils.getWeiboContent(this, tv_content, status.getText());
            tv_content.setText(weiboContent);
        }

        Status retweetedStatus = status.getRetweeted_status();
        if (retweetedStatus != null) {
            include_retweeted_status.setVisibility(View.VISIBLE);
            String retweetContent = "@" + retweetedStatus.getUser().getName() + ":" + retweetedStatus.getText();
            SpannableString weiboContent = StringUtils.getWeiboContent(this, tv_retweeted_content, retweetContent);
            tv_retweeted_content.setText(weiboContent);
            setImages(retweetedStatus, fl_retweeted_imageview, gv_retweeted_images, iv_retweeted_image);
        } else {
            include_retweeted_status.setVisibility(View.GONE);
        }

        // listView headerView - 添加至列表中作为header的菜单栏
        rb_retweets.setText("转发 " + status.getReposts_count());
        rb_comments.setText("评论 " + status.getComments_count());
        rb_likes.setText("赞 " + status.getAttitudes_count());

        tv_share_bottom.setText(status.getReposts_count() == 0 ? "转发" : status.getReposts_count() + "");
        tv_comment_bottom.setText(status.getComments_count() == 0 ? "评论" : status.getComments_count() + "");
        tv_like_bottom.setText(status.getAttitudes_count() == 0 ? "赞" : status.getAttitudes_count() + "");

    }

    private void setImages(final Status status, ViewGroup vgContainer, GridView gvImgs, final ImageView ivImg) {
        if (status == null) {
            return;
        }

        ArrayList<PicUrls> picUrls = status.getPic_urls();
        String picUrl = status.getBmiddle_pic();

        if (picUrls != null && picUrls.size() == 1) {
            vgContainer.setVisibility(View.VISIBLE);
            gvImgs.setVisibility(View.GONE);
            ivImg.setVisibility(View.VISIBLE);
            Glide.with(this).load(picUrl).into(ivImg);
        } else if (picUrls != null && picUrls.size() > 1) {
            vgContainer.setVisibility(View.VISIBLE);
            gvImgs.setVisibility(View.VISIBLE);
            ivImg.setVisibility(View.GONE);
            StatusGridImgsAdapter imagesAdapter = new StatusGridImgsAdapter(this, picUrls);
            gvImgs.setAdapter(imagesAdapter);
        } else {
            vgContainer.setVisibility(View.GONE);
        }
    }

    private void loadComments(final int page) {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        String token = mAccessToken.getToken();
        long id = status.getId();
        RequestParams params = new RequestParams();
        params.put("page", page);
        params.put("access_token", token);
        params.put("id", id);
        AsyncHttpUtils.get("comments/show.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (page == 1) {
                    comments.clear();
                }
                curPage = page;
                CommentsResponse commentsResponse = new Gson().fromJson(responseString, CommentsResponse.class);
                // 更新评论数信息
                tv_comment_bottom.setText(commentsResponse.getTotal_number() == 0 ? "评论" : commentsResponse.getTotal_number() + "");
                rb_comments.setText("评论 " + commentsResponse.getTotal_number());
                // 将获取的评论信息添加到列表上
                addData(commentsResponse);

            }
        });

    }

    private void addData(CommentsResponse response) {
        // 将获取到的数据添加至列表中,重复数据不添加
        for (Comment comment : response.getComments()) {
            if (!comments.contains(comment)) {
                comments.add(comment);
            }
        }
        // 添加完后,通知ListView刷新页面数据
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_comment_bottom:
                Intent intent = new Intent(StatusDetailActivity.this, WriteCommentActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_like_bottom:
                ToastUtils.showToast(StatusDetailActivity.this,"点赞api未开放", Toast.LENGTH_SHORT);
                break;
            case R.id.ll_share_bottom:
                ToastUtils.showToast(StatusDetailActivity.this,"转发", Toast.LENGTH_SHORT);
                break;
        }

    }
}
