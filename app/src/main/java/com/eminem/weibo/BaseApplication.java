package com.eminem.weibo;

import android.app.Application;

import com.eminem.weibo.bean.User;

/**
 * Created by Eminem on 2016/11/30.
 */

public class BaseApplication extends Application {
    public User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
