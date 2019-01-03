package com.sky.happyf.application;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        BGASwipeBackHelper.init(this, null);

        CrashReport.initCrashReport(getApplicationContext(), "3bf0ab6970", false);

        Logger.addLogAdapter(new AndroidLogAdapter());

    }
}
