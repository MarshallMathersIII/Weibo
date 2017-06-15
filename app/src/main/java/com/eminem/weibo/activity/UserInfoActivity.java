package com.eminem.weibo.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.StatusAdapter;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.StatusTimeLineResponse;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.utils.AsyncHttpUtils;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.widget.UnderlineIndicatorView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    // 标题栏
    private View title;
    private ImageView titlebar_iv_left;
    private TextView titlebar_tv;
    // headerView - 用户信息
    private View user_info_head;
    private ImageView iv_avatar;
    private TextView tv_name;
    private TextView tv_follows;
    private TextView tv_fans;
    private TextView tv_sign;
    // shadow_tab - 顶部悬浮的菜单栏
    private View shadow_user_info_tab;
    private RadioGroup shadow_rg_user_info;
    private UnderlineIndicatorView shadow_uliv_user_info;
    private View user_info_tab;
    private RadioGroup rg_user_info;
    private UnderlineIndicatorView uliv_user_info;
    // headerView - 添加至列表中作为header的菜单栏
    private ImageView iv_user_info_head;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lv_user_info;
    // 用户相关信息
    private boolean isCurrentUser;
    private User user;
    private String screenName;
    // 个人微博列表
    private List<Status> statuses = new ArrayList<>();
    //    private StatusAdapter statusAdapter;
    private int curPage = 1;
    // 背景图片最小高度
    private int minImageHeight = -1;
    // 背景图片最大高度
    private int maxImageHeight = -1;

    private int curScrollY;
    private StatusAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        screenName = getIntent().getStringExtra("screen_name");
        if (TextUtils.isEmpty(screenName)) {
            isCurrentUser = true;
            user = application.currentUser;
        }

        initView();
        loadData();
    }

    private void initView() {
        title = new TitleBuilder(this)
                .setTitleText("我的")
                .setLeftImage(R.drawable.navigationbar_back)
                .build();

        // 获取标题栏信息,需要时进行修改
        titlebar_iv_left = (ImageView) title.findViewById(R.id.titlebar_iv_left);
        titlebar_tv = (TextView) title.findViewById(R.id.titlebar_tv);

        initInfoHead();
        initTab();
        initListView();
    }

    private void initInfoHead() {
        iv_user_info_head = (ImageView) findViewById(R.id.iv_user_info_head);

        user_info_head = View.inflate(this, R.layout.user_info_head, null);
        iv_avatar = (ImageView) user_info_head.findViewById(R.id.iv_avatar);
        tv_name = (TextView) user_info_head.findViewById(R.id.tv_name);
        tv_follows = (TextView) user_info_head.findViewById(R.id.tv_follows);
        tv_fans = (TextView) user_info_head.findViewById(R.id.tv_fans);
        tv_sign = (TextView) user_info_head.findViewById(R.id.tv_sign);

    }

    private void initTab() {
        // 悬浮显示的菜单栏
        shadow_user_info_tab = findViewById(R.id.user_info_tab);
        shadow_rg_user_info = (RadioGroup) findViewById(R.id.rg_user_info);
        shadow_uliv_user_info = (UnderlineIndicatorView) findViewById(R.id.uliv_user_info);

        shadow_rg_user_info.setOnCheckedChangeListener(this);
        shadow_uliv_user_info.setCurrentItemWithoutAnim(1);

        // 添加到列表中的菜单栏
        user_info_tab = View.inflate(this, R.layout.user_info_tab, null);
        rg_user_info = (RadioGroup) user_info_tab.findViewById(R.id.rg_user_info);
        uliv_user_info = (UnderlineIndicatorView) user_info_tab.findViewById(R.id.uliv_user_info);

        rg_user_info.setOnCheckedChangeListener(this);
        uliv_user_info.setCurrentItemWithoutAnim(1);
    }

    private void initListView() {
        lv_user_info = (ListView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        adapter = new StatusAdapter(this, statuses);
        lv_user_info.addHeaderView(user_info_head);
        lv_user_info.addHeaderView(user_info_tab);
        lv_user_info.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStatuses(1);
                swipeToLoadLayout.setRefreshing(false);

            }
        });


        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadStatuses(curPage + 1);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });


        iv_user_info_head.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (curScrollY == bottom - oldBottom) {
                    iv_user_info_head.layout(0, 0,
                            iv_user_info_head.getWidth(),
                            oldBottom);
                }
            }
        });

        lv_user_info.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                iv_user_info_head.layout(0,
                        user_info_head.getTop(),
                        iv_user_info_head.getWidth(),
                        user_info_head.getTop() + iv_user_info_head.getHeight());

                if (user_info_head.getBottom() < title.getBottom()) {
                    shadow_user_info_tab.setVisibility(View.VISIBLE);
                    title.setBackgroundResource(R.drawable.navigationbar_background);
                    titlebar_iv_left.setImageResource(R.drawable.navigationbar_back_sel);
                    titlebar_tv.setVisibility(View.VISIBLE);
                } else {
                    shadow_user_info_tab.setVisibility(View.GONE);
                    title.setBackgroundResource(R.drawable.userinfo_navigationbar_background);
                    titlebar_iv_left.setImageResource(R.drawable.userinfo_navigationbar_back_sel);
                    titlebar_tv.setVisibility(View.GONE);
                }

            }
        });
    }


    private void loadData() {
        if (isCurrentUser) {
            // 如果是当前授权用户,直接设置信息
            setUserInfo();
        } else {
            // 如果是查看他人,调用获取用户信息接口
            loadUserInfo();
        }

        // 加载用户所属微博列表
        loadStatuses(1);

    }

    private void setUserInfo() {
        if (user == null) {
            return;
        }
//        tv_name.setText(user.getName());
        Glide.with(this).load(user.getAvatar_hd()).bitmapTransform(new CropCircleTransformation(this)).into(iv_avatar);
        tv_follows.setText("关注 " + user.getFriends_count());
        tv_fans.setText("粉丝 " + user.getFollowers_count());
        tv_sign.setText("简介:" + user.getDescription());
    }

    private void loadUserInfo() {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        String token = mAccessToken.getToken();
        String uid = mAccessToken.getUid();
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("screen_name", screenName);

        AsyncHttpUtils.get("users/show.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("usershow", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                user = new Gson().fromJson(responseString, User.class);
                setUserInfo();


            }
        });
    }

    private void loadStatuses(final int page) {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
        String token = mAccessToken.getToken();
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("screen_name", screenName);
        //此接口最多只返回最新的5条数据，官方移动SDK调用可返回10条；
        AsyncHttpUtils.get("statuses/user_timeline.json", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, responseString);
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

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        syncRadioButton(group, checkedId);

    }

    private void syncRadioButton(RadioGroup group, int checkedId) {
        int index = group.indexOfChild(group.findViewById(checkedId));

        if (shadow_user_info_tab.getVisibility() == View.VISIBLE) {
            shadow_uliv_user_info.setCurrentItem(index);

            ((RadioButton) rg_user_info.findViewById(checkedId)).setChecked(true);
            uliv_user_info.setCurrentItemWithoutAnim(index);
        } else {
            uliv_user_info.setCurrentItem(index);

            ((RadioButton) shadow_rg_user_info.findViewById(checkedId)).setChecked(true);
            shadow_uliv_user_info.setCurrentItemWithoutAnim(index);
        }

    }
}
