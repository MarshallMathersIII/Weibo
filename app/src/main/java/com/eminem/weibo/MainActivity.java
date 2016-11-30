package com.eminem.weibo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.eminem.weibo.fragment.FragmentController;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fl_content)
    FrameLayout flContent;
    @Bind(R.id.rb_home)
    RadioButton rbHome;
    @Bind(R.id.rb_message)
    RadioButton rbMessage;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.rb_serach)
    RadioButton rbSerach;
    @Bind(R.id.rb_user)
    RadioButton rbUser;
    @Bind(R.id.rg)
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
