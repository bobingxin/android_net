package com.dreamiii.net_android;

import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by bbx on 2016/11/7.
 */

public class BaseActivity extends AppCompatActivity {

    //用来防止内存泄露，同意管理网络访问的取消
    protected CompositeSubscription mSubscription = new CompositeSubscription();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用来防止内存泄露
        mSubscription.unsubscribe();
    }
}
