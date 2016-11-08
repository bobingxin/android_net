package com.dreamiii.net_android;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dreamiii.net_android.net.TestClient;
import com.dreamiii.net_android.net.TestModel;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = (TextView) findViewById(R.id.tv_test);
        tvTest.setOnClickListener(this);
        Subscription subscription = TestClient.test(new Subscriber<TestModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(TestModel testModel) {
                Log.e(TAG,testModel.toString());
                if(tvTest==null){
                    Log.e(TAG,"test null");
                }else {
                    Log.e(TAG,"test not null");
                    tvTest.setText(testModel.toString());
                }
            }
        });
        mSubscription.add(subscription);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_test:
                finish();
                Intent intent = new Intent(this,LeakedActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
