package com.antimage.videocompress.base;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by xuyuming on 2018/5/17.
 */

public class BasePresenterImpl<T extends AppCompatActivity, V extends BaseView> implements BasePresenter {

    protected T activity;
    protected V mView;
    protected String TAG;

    public BasePresenterImpl(T activity, V mView) {
        this.activity = activity;
        this.mView = mView;
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
