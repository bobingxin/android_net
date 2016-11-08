package com.dreamiii.net_android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected CompositeSubscription mSubscription;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSubscription = new CompositeSubscription();
        return onNewCreateView(inflater,container,savedInstanceState);
    }

    protected abstract View onNewCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscription.unsubscribe();
    }
}
