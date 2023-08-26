package com.tatv.baseapp.utils.system;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class StorageUtil {

    /**
     * Kiểm tra dung lượng bộ nhớ
     * return KB
     * */
    public static double getStorageAvailable(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (stat.getBlockSizeLong() * stat.getAvailableBlocksLong())/1024;
    }

    /**
     * Kiểm tra kích thước file
     * return KB
     * */
    public static double getFileSize(File file){
        return Integer.parseInt(String.valueOf(file.length()/1024));
    }
}
