package com.dreamiii.network;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by bbx on 2016/10/18.
 * 网络访问的基础框架
 */

public class ServiceGenerator {

    public static final int DEFAULT_MILLISECONDS = 60000;       //默认的超时时间
    private static boolean isDebug;

    public static String apiBaseUrl = "http://10.0.0.15:8089";

    private static Context context;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());



    private static OkHttpClient.Builder okHttpClientBuilder;

    public static void init(Application app){
        context = app;
    }

    /** 获取全局上下文 */
    public static Context getContext() {
        if (context == null) throw new IllegalStateException("请先在全局Application中调用 init(context) 初始化！");
        return context;
    }


    private ServiceGenerator() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    /**
     * 用来改变baseurl
     * @param newApiBaseUrl 新的api url
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * 普通的创建服务的方法
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createSpecialService(Class<S> serviceClass){

//        addLogging();
        OkHttpClient client = okHttpClientBuilder.build();
        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);

    }

    /**
     * 普通的创建服务的方法
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass){

//        addLogging();
        OkHttpClient client = okHttpClientBuilder.build();
//        builder.addConverterFactory(GsonConverterFactory.create());
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
            okHttpClientBuilder.addInterceptor(new Interceptor() {
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
            okHttpClientBuilder.addInterceptor(new Interceptor() {
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

        addLogging();

        OkHttpClient client = okHttpClientBuilder.build();
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


    public static void addLogging(){
        if(isDebug){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(logging);
        }
    }

    private static class ServiceGeneratorHolder {
        private static ServiceGenerator holder = new ServiceGenerator();
    }


    public static ServiceGenerator getInstance(){
        return ServiceGeneratorHolder.holder;
    }

    public static void debug(boolean isDebug){
        ServiceGenerator.isDebug = isDebug;
    }

    public ServiceGenerator openCookieStore(){
        okHttpClientBuilder.cookieJar(new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(context)));
        return this;
    }


    public ServiceGenerator setConnectTimeout(int second){
        okHttpClientBuilder.connectTimeout(second,TimeUnit.SECONDS);
        return this;
    }

    public ServiceGenerator setReadTimeout(int second){
        okHttpClientBuilder.readTimeout(second,TimeUnit.SECONDS);
        return this;
    }

    public ServiceGenerator setWriteTimeout(int second){
        okHttpClientBuilder.writeTimeout(second,TimeUnit.SECONDS);
        return this;
    }

    public ServiceGenerator setHeader(final String key, final String value){
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header(key, value)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return this;
    }

    /** https的全局自签名证书 */
    public ServiceGenerator setCertificates(InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /** https双向认证证书 */
    public ServiceGenerator setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    public ServiceGenerator setUnAuthCertificates(){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClientBuilder.sslSocketFactory(sslSocketFactory);
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
