package com.eminem.weibo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.eminem.weibo.bean.User;
import com.eminem.weibo.utils.AppManager;
import com.githang.androidcrash.AndroidCrash;
import com.githang.androidcrash.reporter.httpreporter.CrashHttpReporter;
import com.githang.androidcrash.reporter.mailreporter.CrashEmailReporter;

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
//        initHttpReporter();

    }


    private static BaseApplication mInstance;

    public static BaseApplication getContext() {
        if (mInstance == null) {
            mInstance = new BaseApplication();
        }
        return mInstance;
    }

    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        CrashEmailReporter reporter = new CrashEmailReporter(this);
        reporter.setReceiver("你的接收邮箱");
        reporter.setSender("irain_log@163.com");
        reporter.setSendPassword("xxxxxxxx");
        reporter.setSMTPHost("smtp.163.com");
        reporter.setPort("465");
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }

    /**
     * 使用HTTP发送日志
     */
    private void initHttpReporter() {
        CrashHttpReporter reporter = new CrashHttpReporter(this) {
            /**
             * 重写此方法，可以弹出自定义的崩溃提示对话框，而不使用系统的崩溃处理。
             * @param thread
             * @param ex
             */
            @Override
            public void closeApp(Thread thread, Throwable ex) {
                final Activity activity = AppManager.currentActivity();
                Toast.makeText(activity, "发生异常，正在退出", Toast.LENGTH_SHORT).show();
                // 自定义弹出对话框
                new AlertDialog.Builder(activity).
                        setMessage("程序发生异常，现在退出").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppManager.AppExit(activity);
                            }
                        }).create().show();
                Log.d("MyApplication", "thead:" + Thread.currentThread().getName());
            }
        };
        reporter.setUrl("http://crashreport.jd-app.com/your_receiver").setFileParam("fileName")
                .setToParam("to").setTo("ghy1060627080@163.com")
                .setTitleParam("subject").setBodyParam("message");
        reporter.setCallback(new CrashHttpReporter.HttpReportCallback() {
            @Override
            public boolean isSuccess(int i, String s) {
                return s.endsWith("ok");
            }
        });
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);

    }
}
