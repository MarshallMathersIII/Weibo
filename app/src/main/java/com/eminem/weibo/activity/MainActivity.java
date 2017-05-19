package com.eminem.weibo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.fragment.FragmentController;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_message)
    RadioButton rbMessage;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.rb_serach)
    RadioButton rbSerach;
    @BindView(R.id.rb_user)
    RadioButton rbUser;
    @BindView(R.id.rg)
    RadioGroup rg;


    private FragmentController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        controller=FragmentController.getInstance(this,R.id.fl_content);
        controller.showFragment(0);
    }



    @OnClick({R.id.rb_home, R.id.rb_message, R.id.iv_add, R.id.rb_serach, R.id.rb_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_message:
                controller.showFragment(1);
                break;
            case R.id.iv_add:
                Toast.makeText(this,"bilibili",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_serach:
                controller.showFragment(2);
                break;
            case R.id.rb_user:
                controller.showFragment(3);
                break;
        }
    }
}
