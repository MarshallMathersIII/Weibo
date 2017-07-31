package com.eminem.weibo;

import android.app.Application;

import com.eminem.weibo.bean.User;

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
