package com.antimage.videocompress.utils;

import android.util.Log;

/**
 * Created by xuyuming on 2018/5/17.
 */

public class MLog {

    public static final boolean DEBUG = true;
    private static String TAG = "MVideoCompress";

    public static void i(String className, String msg) {
        if (!DEBUG) return;
        if (null == msg || "".equals(msg.trim())) {
            msg = "null";
        }
        msg = String.format("%s : \"%s\"", className, msg);
        Log.i(TAG, msg);
    }

    public static void d(String className, String msg) {
        if (!DEBUG) return;
        if (null == msg || "".equals(msg.trim())) {
            msg = "null";
        }
        msg = String.format("%s : \"%s\"", className, msg);
        Log.d(TAG, msg);

    }

    public static void e(String className, String msg) {
        if (!DEBUG) return;
        if (null == msg || "".equals(msg.trim())) {
            msg = "null";
        }
        msg = String.format("%s : \"%s\"", className, msg);
        Log.e(TAG, msg);

    }

    public static void w(String className, String msg) {
        if (!DEBUG) return;
        if (null == msg || "".equals(msg.trim())) {
            msg = "null";
        }
        msg = String.format("%s : \"%s\"", className, msg);
        Log.w(TAG, msg);
    }
}
