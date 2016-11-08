package com.dreamiii.net_android.net;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by bbx on 2016/11/7.
 */

public interface TestService {

    @GET("test.php")
    Observable<TestModel> test();
}
