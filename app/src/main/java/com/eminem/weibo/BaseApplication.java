package com.eminem.weibo;

import android.app.Application;
import android.content.Context;

import com.eminem.weibo.bean.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Eminem on 2016/11/30.
 */

public class BaseApplication extends Application {
    public User currentUser;
    private static BaseApplication mApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        this.mApplication = this;
        initImageLoader(this);


    }

    // 初始化图片处理
    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .defaultDisplayImageOptions(ImageOptHelper.getImgOptions())
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


//    public static Context getContext() {
//        return mApplication.getApplicationContext();
//
//    }

    private static BaseApplication mInstance;

    public static  BaseApplication getContext(){
        if(mInstance == null){
            mInstance = new BaseApplication();
        }
        return mInstance;
    }

}
