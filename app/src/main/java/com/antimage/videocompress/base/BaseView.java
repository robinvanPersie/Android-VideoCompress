package com.antimage.videocompress.base;

/**
 * Created by xuyuming on 2018/5/17.
 */

public interface BaseView {

    void toast(int msgId);

    void toast(String msg);

    void showLoadingDialog();

    void dismissLoadingDialog();
}
