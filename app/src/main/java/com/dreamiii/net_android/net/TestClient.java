package com.dreamiii.net_android.net;

import com.dreamiii.network.ServiceGenerator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bbx on 2016/11/7.
 */

public class TestClient {

    public static Subscription test(Subscriber<TestModel> subscriber){
        return ServiceGenerator.createService(TestService.class).test()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
