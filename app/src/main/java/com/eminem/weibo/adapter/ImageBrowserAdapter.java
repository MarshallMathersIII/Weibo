package com.eminem.weibo.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.eminem.weibo.R;
import com.eminem.weibo.bean.PicUrls;
import com.eminem.weibo.utils.DisplayUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private ArrayList<PicUrls> picUrls;
    private ArrayList<View> picViews;
    public ImageBrowserAdapter(Activity context, ArrayList<PicUrls> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
        initImgs();
    }

    private void initImgs() {
        picViews = new ArrayList<View>();

        for (int i = 0; i < picUrls.size(); i++) {
            // 填充显示图片的页面布局
            View view = View.inflate(context, R.layout.item_image_browser, null);
            picViews.add(view);
        }
    }

    @Override
    public int getCount() {
        if (picUrls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        int index = position % picUrls.size();
        View view = picViews.get(index);
        final PhotoView iv_image_browser = (PhotoView) view.findViewById(R.id.iv_image_browser);
        PicUrls picUrl = picUrls.get(index);
//      String url = picUrl.isShowOriImag() ? picUrl.getOriginal_pic() : picUrl.getBmiddle_pic();
        String url = picUrl.getOriginal_pic();

        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        float scale = (float) resource.getHeight() / resource.getWidth();
                        int screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);
                        int screenHeightPixels = DisplayUtils.getScreenHeightPixels(context);
                        int height = (int) (screenWidthPixels * scale);

                        if (height < screenHeightPixels) {
                            height = screenHeightPixels;
                        }

                        ViewGroup.LayoutParams params = iv_image_browser.getLayoutParams();
                        params.height = height;
                        params.width = screenWidthPixels;

                        iv_image_browser.setImageBitmap(resource);
                    }
                });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public PicUrls getPic(int position) {
        return picUrls.get(position % picUrls.size());
    }

    public Bitmap getBitmap(int position) {
        Bitmap bitmap = null;
        View view = picViews.get(position % picViews.size());
        ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
        Drawable drawable = iv_image_browser.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bitmap = bd.getBitmap();
        }
        return bitmap;
    }


}
