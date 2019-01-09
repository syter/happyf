package com.sky.happyf.application;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.sky.happyf.util.Constants;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        BGASwipeBackHelper.init(this, null);

        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APPKEY, false);

        Logger.addLogAdapter(new AndroidLogAdapter());

    }
}
