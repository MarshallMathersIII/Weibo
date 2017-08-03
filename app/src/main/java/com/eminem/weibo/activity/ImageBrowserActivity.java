package com.eminem.weibo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.ImageBrowserAdapter;
import com.eminem.weibo.bean.PicUrls;
import com.eminem.weibo.bean.Status;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.eminem.weibo.R.id.tv_image_index;
import static com.eminem.weibo.R.id.vp_image_brower;

public class ImageBrowserActivity extends BaseActivity {

    @BindView(vp_image_brower)
    ViewPager vpImageBrower;
    @BindView(tv_image_index)
    TextView tvImageIndex;
    @BindView(R.id.iv_more)
    ImageView ivMore;


    private Status status;
    private int position;
    private ImageBrowserAdapter adapter;
    private ArrayList<PicUrls> imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        ButterKnife.bind(this);
        initData();
        setData();
    }

    private void initData() {
        status = (Status) getIntent().getSerializableExtra("status");
        position = getIntent().getIntExtra("position", 0);
        // 获取图片数据集合(单图也有对应的集合,集合的size为1)
        imgUrls = status.getPic_urls();

    }

    private void setData() {
        adapter = new ImageBrowserAdapter(this, imgUrls);
        vpImageBrower.setAdapter(adapter);

        final int size = imgUrls.size();
        int initPosition = Integer.MAX_VALUE / 2 / size * size + position;

        if(size > 1) {
            tvImageIndex.setVisibility(View.VISIBLE);
            tvImageIndex.setText((position+1) + "/" + size);
        } else {
            tvImageIndex.setVisibility(View.GONE);
        }

        vpImageBrower.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int index = arg0 % size;
                tvImageIndex.setText((index+1) + "/" + size);

                PicUrls pic = adapter.getPic(arg0);
//                btn_original_image.setVisibility(pic.isShowOriImag() ?
//                        View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        vpImageBrower.setCurrentItem(initPosition);

    }



}
