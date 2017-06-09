package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eminem.weibo.BaseApplication;
import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.UserItemAdapter;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.bean.UserItem;
import com.eminem.weibo.constants.AccessTokenKeeper;
import com.eminem.weibo.utils.AsyncHttpUtils;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;
import com.eminem.weibo.widget.WrapHeightListView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Eminem on 2016/11/30.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_userinfo;

    private ImageView iv_avatar;
    private TextView tv_subhead;
    private TextView tv_caption;

    private TextView tv_status_count;
    private TextView tv_follow_count;
    private TextView tv_fans_count;

    private WrapHeightListView lv_user_items;

    private User user;

    Unbinder unbinder;
    private View view;
    private List<UserItem> userItems;
    private UserItemAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_user, null);
        initView();
        setItem();

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        String token = mAccessToken.getToken();
        String uid = mAccessToken.getUid();
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        params.put("uid", uid);
        if (!hidden) {
            AsyncHttpUtils.get("users/show.json", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("usershow",responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    ToastUtils.showToast(activity, "onSuccess", Toast.LENGTH_SHORT);
                    Log.d("usershow",responseString);
                    BaseApplication application = (BaseApplication) activity.getApplication();
                    application.currentUser = new Gson().fromJson(responseString, User.class);
                    setUserInfo();
                }
            });


        }
    }

    private void initView() {
        new TitleBuilder(view)
                .setTitleText("我")
                .setLeftText("添加好友")
                .setRightText("设置")
                .setRightOnClickListener(this)
                .setLeftOnClickListener(this);
        tv_status_count = (TextView) view.findViewById(R.id.tv_status_count);
        tv_follow_count = (TextView) view.findViewById(R.id.tv_follow_count);
        tv_fans_count = (TextView) view.findViewById(R.id.tv_fans_count);

        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_subhead = (TextView) view.findViewById(R.id.tv_subhead);
        tv_caption = (TextView) view.findViewById(R.id.tv_caption);

        lv_user_items = (WrapHeightListView) view.findViewById(R.id.lv_user_items);
        userItems = new ArrayList<>();
        adapter = new UserItemAdapter(activity, userItems);
        lv_user_items.setAdapter(adapter);
    }

    private void setItem() {
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_1, "新的朋友", ""));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_2, "微博等级", "Lv13"));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_3, "编辑资料", ""));
        userItems.add(new UserItem(true, R.drawable.push_icon_app_small_4, "我的相册", "(18)"));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_5, "我的点评", ""));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_4, "我的赞", "(32)"));
        userItems.add(new UserItem(true, R.drawable.push_icon_app_small_3, "微博支付", ""));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_2, "微博运动", "步数、卡路里、跑步轨迹"));
        userItems.add(new UserItem(true, R.drawable.push_icon_app_small_1, "更多", "收藏、名片"));
        adapter.notifyDataSetChanged();
    }

    private void setUserInfo() {
        user = ((BaseApplication)activity.getApplication()).currentUser;

        if(user == null) {
            return;
        }

        // set data
        tv_subhead.setText(user.getName());
        tv_caption.setText("简介:" + user.getDescription());
        Glide.with(activity).load(user.getAvatar_hd()).bitmapTransform(new CropCircleTransformation(activity)).into(iv_avatar);
        tv_status_count.setText("" + user.getStatuses_count());
        tv_follow_count.setText("" + user.getFriends_count());
        tv_fans_count.setText("" + user.getFollowers_count());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_iv_left:
                break;
            case R.id.titlebar_iv_right:
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
