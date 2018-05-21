package com.antimage.videocompress.module.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.antimage.videocompress.R;
import com.antimage.videocompress.base.BaseActivity;
import com.antimage.videocompress.module.contract.MainContract;
import com.antimage.videocompress.module.presenter.MainPresenter;
import com.antimage.videocompress.utils.MLog;
import com.antimage.videocompress.utils.videoconverter.IVideoCompress;
import com.antimage.videocompress.widget.MIActionSheet;
import com.antimage.videocompress.widget.MenuItemClickListener;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by xuyuming on 2018/5/17.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView
        , View.OnClickListener, MenuItemClickListener, EasyPermissions.PermissionCallbacks
        , IVideoCompress {

    private static final int STOP = 0;
    private static final int RUNNING = 1;
    private int status = STOP;

    private int[] itemArray = {R.string.video_record, R.string.video_choose};

    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void initView() {
        textView = findViewById(R.id.progress_text_id);
        progressBar = findViewById(R.id.progress_bar_id);
        findViewById(R.id.menu_btn).setOnClickListener(this);
        findViewById(R.id.cancel_btn).setOnClickListener(this);
        findViewById(R.id.play_btn).setOnClickListener(this);
        findViewById(R.id.stop_btn).setOnClickListener(this);
        mPresenter = new MainPresenter(this, this);
        mPresenter.onCreate();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_btn:
                if (status == RUNNING) {
                    toast("有正在执行的任务");
                    break;
                }
                if (!requestPermission()) break;
                showActionSheet();
                break;
            case R.id.cancel_btn:
                mPresenter.cancelCurrentTask();
                break;
            case R.id.play_btn:
                mPresenter.initVideoData();
                break;
            case R.id.stop_btn:
                stopVideo();
                break;
            default:
                break;
        }
    }

    private MIActionSheet actionSheet;

    public void showActionSheet() {
        if (actionSheet == null) {
            actionSheet = new MIActionSheet(this);
            actionSheet.setItemArray(itemArray);
            actionSheet.setMenuItemClickListener(this);
        }
        actionSheet.show();
    }

    public void dismissActionSheet() {
        if (actionSheet != null && actionSheet.isShowing()) {
            actionSheet.dismissMenu();
        }
    }

    public static final int REQUEST_PERMISSION = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//        switch (requestCode) {
//            case REQUEST_PERMISSION:
//                for (int i : grantResults) {
//                    MLog.w(getLocalClassName(), " i = " + i);
//                }
//                for (String p : permissions) {
//                    MLog.i(getLocalClassName(), " p = " + p);
//                }
//
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onItemClick(int resId) {
        dismissActionSheet();
        switch (resId) {
            case R.string.video_record:
                takeVideo();
                break;
            case R.string.video_choose:
                chooseVideo();
                break;
        }
    }

    private void takeVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQ_TAKE_VIDEO);
    }

    private void chooseVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQ_CHOOSE_VIDEO);
    }

    private static final int REQ_TAKE_VIDEO = 0;
    private static final int REQ_CHOOSE_VIDEO = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        MLog.d(getLocalClassName(), "onPermissionsGranted()");
    }

    /**
     * EasyPermissions.requestPermissions()的回调, onRequestPermissionResult()在此之后
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        MLog.d(getLocalClassName(), "onPermissionsDenied()");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION)
    public boolean requestPermission() {
        return mPresenter.requestPermission();
    }

    @Override
    public void onPrePared() {
        status = RUNNING;
        toast("准备转换");
    }

    @Override
    public void onSuccess(String sourcePath, String newPath) {
        status = STOP;
        toast("转换成功 视频在：" + newPath);
    }

    @Override
    public void onFail() {
        status = STOP;
        toast("失败");
    }

    @Override
    public void onProgress(int percent) {
        progressBar.setProgress(percent);
        textView.setText(percent + "%");
    }

    @Override
    public void resetView() {
        progressBar.setProgress(0);
        textView.setText(null);
        status = STOP;
    }

    @Override
    public void playVideo(String url) {
        if (TextUtils.isEmpty(url)) {
            toast("请先压缩一个视频，再播放");
            return;
        }
        initViewVideo(url);
    }

    private VideoView videoView;

    private void initViewVideo(String url) {
        if (videoView == null) {
            videoView = findViewById(R.id.video_view_id);
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.setVideoURI(Uri.fromFile(new File(url)));
    }

    private void stopVideo() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
