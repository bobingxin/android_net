package com.dreamiii.net_android;

import android.app.Application;
import android.content.Context;

import com.dreamiii.network.ServiceGenerator;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by bbx on 2016/11/7.
 */

public class TestApplication extends Application {
    private RefWatcher refWatcher;

    private static final int READWRITETIMEOUT = 15;
    private static final int CONNECTTIMEOUT = 15;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

        //网络请求的设置
        ServiceGenerator.init(this);
        ServiceGenerator.debug(true);
        ServiceGenerator.getInstance()
                .openCookieStore()
                .setConnectTimeout(CONNECTTIMEOUT)
                .setWriteTimeout(READWRITETIMEOUT)
                .setReadTimeout(READWRITETIMEOUT)
                .setCertificates();
        ServiceGenerator.changeApiBaseUrl("http://10.0.0.19:8888/");
    }

    public static RefWatcher getRefWatcher(Context context) {
        TestApplication application = (TestApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
