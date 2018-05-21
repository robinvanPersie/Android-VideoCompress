package com.antimage.videocompress.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by xuyuming on 2018/5/18.
 */

public class FileUtils {

    public static File createFile(String path) {
        File file = new File(path);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
