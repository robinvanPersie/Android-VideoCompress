package com.antimage.videocompress.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.antimage.videocompress.widget.AppProgressDialog;

/**
 * Created by xuyuming on 2018/5/17.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData(savedInstanceState);
        initView();
    }

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    @Override
    public void toast(int msgId) {
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissProgressDialog();
    }

    private AppProgressDialog progressDialog;

    private void showProgressDialog(boolean cancel) {
        if (progressDialog == null) {
            progressDialog = new AppProgressDialog();
        }
        if (!isFinishing()) progressDialog.show(this, cancel);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
