package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;

/**
 * Created by Eminem on 2016/11/30.
 *
 */

public class MessageFragment extends BaseFragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= View.inflate(activity,R.layout.frag_message,null);
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


        return view;
    }
}
