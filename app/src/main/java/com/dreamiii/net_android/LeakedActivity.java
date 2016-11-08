package com.dreamiii.net_android;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LeakedActivity extends BaseActivity {

    private static final String TAG = LeakedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"haha");
            }
        },60000);
    }
}
