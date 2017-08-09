package com.eminem.weibo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eminem.weibo.R;
import com.eminem.weibo.utils.TitleBuilder;

public class MessageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        initView();
        loadData();
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
    }

    private void loadData() {

    }
}
