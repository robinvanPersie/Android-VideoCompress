package com.antimage.videocompress.utils;

import android.os.Build;

/**
 * Created by xuyuming on 2018/5/18.
 */

public class DeviceUtils {

    /**
     * 是否使用软编码。
     * 默认情况，视频的编码，解码都是使用hardware，目前发现华为的硬件编码出的视频在iOS和OS X上播放有一半绿屏,
     * 网上查了相关内容，因为视频的 1 frame 里包含了多个 slice, 而iOS和OS X只支持 1 frame 里包含 1 slice的视频，
     * 多个slice的导致不能正确解析，所以绿屏，而Android默认都支持。
     * @return true: use software, false: use hardware
     */
    public static boolean useSoftware() {
        return Build.MANUFACTURER.equalsIgnoreCase("HUAWEI");
    }
}
