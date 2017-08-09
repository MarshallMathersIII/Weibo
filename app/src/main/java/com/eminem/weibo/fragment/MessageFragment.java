package com.eminem.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.activity.MessageDetailsActivity;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;

/**
 * Created by Eminem on 2016/11/30.
 *
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout ll_message_at;
    private LinearLayout ll_message_comment;
    private LinearLayout ll_message_like;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        return view;
    }

    private void initView() {
        view = View.inflate(activity, R.layout.frag_message, null);
        new TitleBuilder(view)
                .setTitleText("消息")
                .setLeftText("发现群")
                .setRightImage(R.drawable.title_message_icon)
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(activity, "click", Toast.LENGTH_SHORT);
                    }
                })
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(activity, "发现群", Toast.LENGTH_SHORT);
                    }
                });
        ll_message_at = (LinearLayout) view.findViewById(R.id.ll_message_at);
        ll_message_comment = (LinearLayout) view.findViewById(R.id.ll_message_comment);
        ll_message_like = (LinearLayout) view.findViewById(R.id.ll_message_like);
        ll_message_at.setOnClickListener(this);
        ll_message_comment.setOnClickListener(this);
        ll_message_like.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_message_at:
                startActivity(new Intent(getActivity(), MessageDetailsActivity.class));
                break;
            case R.id.ll_message_comment:
                startActivity(new Intent(getActivity(), MessageDetailsActivity.class));
                break;
            case R.id.ll_message_like:
                ToastUtils.showToast(getActivity(), "接口未开放", Toast.LENGTH_LONG);
                break;
        }
    }
}
