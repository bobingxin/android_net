package com.dreamiii.network;

import android.os.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bbx on 2016/10/18.
 * 网络访问的基础框架
 */

public class ServiceGenerator {

    public static String apiBaseUrl = "http://10.0.0.15:8089";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());


    private static OkHttpClient.Builder okHttpBuilder;

    /**
     * 用来改变baseurl
     * @param newApiBaseUrl
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * okhttp的builder
     * @param okbuilder
     */
    public static void initClient(OkHttpClient.Builder okbuilder){
        okHttpBuilder = okbuilder;
    }

    /**
     * 普通的创建服务的方法
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass){
        OkHttpClient client = okHttpBuilder.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);

    }


    /**
     * 特殊的创建服务的方法，有设备id和token的
     * @param serviceClass
     * @param token
     * @param <S>
     * @return
     */
    public static <S> S ctreateService(Class<S> serviceClass,final String token){
        if(token != null){
            okHttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("dvid", Build.SERIAL)
                            .header("token", token)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }else{
            okHttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("dvid", Build.SERIAL)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = okHttpBuilder.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static OkHttpClient.Builder getOkhttpClient(){
        return okHttpBuilder;
    }

}
