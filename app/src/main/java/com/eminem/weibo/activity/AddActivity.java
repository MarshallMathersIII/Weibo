package com.eminem.weibo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.AddPagerAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class AddActivity extends BaseActivity {
    private ArrayList<View> viewList = new ArrayList<>();
    private ViewPager mViewger;
    private AddPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        View view1 = getLayoutInflater().inflate(R.layout.view_add, null);
        View view2 = getLayoutInflater().inflate(R.layout.view_add2, null);
        viewList.add(view1);
        viewList.add(view2);
        initView();
    }

    private void initView() {
        adapter=new AddPagerAdapter(viewList);
        mViewger= (ViewPager) findViewById(R.id.mViewPager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        mViewger.setAdapter(adapter);
        indicator.setViewPager(mViewger);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());


    }
}
