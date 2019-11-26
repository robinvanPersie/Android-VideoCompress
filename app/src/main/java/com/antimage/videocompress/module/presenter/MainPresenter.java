package com.antimage.videocompress.module.presenter;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import com.antimage.vccompress.MLog;
import com.antimage.vccompress.videoconverter.VideoCompressTask;
import com.antimage.videocompress.base.BasePresenterImpl;
import com.antimage.videocompress.module.contract.MainContract;
import com.antimage.videocompress.module.ui.MainActivity;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by xuyuming on 2018/5/17.
 */

public class MainPresenter extends BasePresenterImpl<MainActivity, MainContract.MainView> implements MainContract.IMainPresenter {

    private String destPath;

    public MainPresenter(MainActivity activity, MainContract.MainView view) {
        super(activity, view);
    }

    @Override
    public void onDestroy() {
        cancelCurrentTask();
        activity = null;
        mView = null;
    }

    @Override
    public void onCreate() {
//        activity.requestPermission();
    }

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    public boolean requestPermission() {
        if (!EasyPermissions.hasPermissions(activity, permissions)) {
            EasyPermissions.requestPermissions(activity, "必须的权限", MainActivity.REQUEST_PERMISSION, permissions);
            return false;
        } else {
            mView.toast("已有权限");
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (activity.RESULT_OK != resultCode) return;
        Uri uri = data.getData();
        MLog.i(TAG, "uri = " + uri.toString());
        String[] filePathColumn = { MediaStore.Video.Media.DATA };

        Cursor cursor = activity.getContentResolver().query(uri ,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String videoPath = cursor.getString(columnIndex);
        MLog.w(TAG, "video Path = " + videoPath);
        cursor.close();
        compress(videoPath);
    }

    private VideoCompressTask task;

    private void compress(String sourcePath) {
        destPath = String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/tmp.mp4");
        task = new VideoCompressTask(sourcePath, destPath, activity);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void cancelCurrentTask() {
        if (task != null && !task.isCancelled() && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancelTask();
            mView.resetView();
        }
    }

    @Override
    public void initVideoData() {
        mView.playVideo(destPath);
    }
}
