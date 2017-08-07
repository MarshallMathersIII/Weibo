package com.eminem.weibo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.ImageBrowserAdapter;
import com.eminem.weibo.bean.PicUrls;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.bottomdialog.BottomDialog;

import static com.eminem.weibo.R.id.tv_image_index;
import static com.eminem.weibo.R.id.vp_image_brower;

public class ImageBrowserActivity extends BaseActivity implements View.OnClickListener {

    @BindView(vp_image_brower)
    ViewPager vpImageBrower;
    @BindView(tv_image_index)
    TextView tvImageIndex;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    private TextView tv_save_pic;
    private TextView tv_like;
    private TextView tv_retweeted;
    private TextView tv_cancel;
    private TextView tv_orginal_pic;
    private BaseBottomDialog dialog;
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
        if (size > 1) {
            tvImageIndex.setVisibility(View.VISIBLE);
            tvImageIndex.setText((position + 1) + "/" + size);
        } else {
            tvImageIndex.setVisibility(View.GONE);
        }

        vpImageBrower.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                int index = arg0 % size;
                tvImageIndex.setText((index + 1) + "/" + size);

//                PicUrls pic = adapter.getPic(arg0);
//                tv_orginal_pic.setVisibility(pic.isShowOriImag() ?
//                        View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        vpImageBrower.setCurrentItem(initPosition);

    }


    @OnClick(R.id.iv_more)
    public void onViewClicked() {
        dialog = BottomDialog.create(getSupportFragmentManager())
                .setLayoutRes(R.layout.bottom_dialog_layout)      // dialog layout
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        initView(v);
                    }
                })
                .show();
    }

    private void initView(View view) {
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_retweeted = (TextView) view.findViewById(R.id.tv_retweeted);
        tv_like = (TextView) view.findViewById(R.id.tv_like);
        tv_save_pic = (TextView) view.findViewById(R.id.tv_save_pic);
        tv_orginal_pic = (TextView) view.findViewById(R.id.tv_orginal_pic);
        tv_save_pic.setOnClickListener(this);
        tv_retweeted.setOnClickListener(this);
        tv_like.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_orginal_pic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        PicUrls picUrl = adapter.getPic(vpImageBrower.getCurrentItem());
        switch (v.getId()) {
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_retweeted:
                ToastUtils.showToast(this, "转发", Toast.LENGTH_LONG);
                break;
            case R.id.tv_like:
                ToastUtils.showToast(this, "点赞", Toast.LENGTH_LONG);
                break;
            case R.id.tv_orginal_pic:
                ToastUtils.showToast(this, "查看原图", Toast.LENGTH_LONG);
                break;
            case R.id.tv_save_pic:
                ToastUtils.showToast(this, "保存图片", Toast.LENGTH_LONG);
                Bitmap bitmap = adapter.getBitmap(vpImageBrower.getCurrentItem());

                boolean showOriImag = picUrl.isShowOriImag();
                String fileName = "img-" + (showOriImag ? "ori-" : "mid-") + picUrl.getImageId();

                String title = fileName.substring(0, fileName.lastIndexOf("."));
                String insertImage = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, title, "EminemWeiboImage");
                if (insertImage == null) {
                    showToast("图片保存失败");
                    dialog.dismiss();
                } else {
                    showToast("图片保存成功");
                    dialog.dismiss();
                }
                break;
        }

    }


}
