package com.antimage.videocompress.module.contract;

import android.content.Intent;

import com.antimage.videocompress.base.BasePresenter;
import com.antimage.videocompress.base.BaseView;

/**
 * Created by xuyuming on 2018/5/17.
 */

public interface MainContract {

    interface MainView extends BaseView {

        void resetView();

        void playVideo(String url);
    }

    interface IMainPresenter extends BasePresenter {

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void cancelCurrentTask();

        void initVideoData();
    }
}
