package com.dreamiii.net_android;

import android.app.Application;
import android.content.Context;

import com.dreamiii.network.ServiceGenerator;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by bbx on 2016/11/7.
 */

public class TestApplication extends Application {
    private RefWatcher refWatcher;

    private static final int READTIMEOUT = 60;
    private static final int CONNECTTIMEOUT = 60;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READTIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTTIMEOUT,TimeUnit.SECONDS)
                .cookieJar(new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(this)));
        ServiceGenerator.changeApiBaseUrl("http://10.0.0.22:8888/");
        ServiceGenerator.initClient(builder);
    }

    public static RefWatcher getRefWatcher(Context context) {
        TestApplication application = (TestApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
