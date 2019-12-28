/*
 * fileName: Utils.java
 *
 * author:  yangf
 *
 * createTime: 2013-12-04
 * 
 */

package com.zp.mobile91q;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * 工具类
 * 
 * @version [V1.0, 2013-12-04]
 */

public class Utils {

    private static final String TAG = "Utils";

    public static void print(String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
    }

    /**
     * 检测文件是否存在
     * 
     * @param file 需要检测的文件
     * @return true：存在 false:不存在
     * @see [类、类#方法、类#成员]
     */

    public static boolean checkFile(String file) {
        // File temp = new File(ScreensFlyConstants.PATH + "DLNA.xml");
        Log.d(TAG, "checking file:" + file);
        File temp = new File(file);
        boolean rec = temp.exists();
        return rec;
    }

    /**
     * 将文件列表的所有文件拷贝到指定目录
     * 
     * @param context Android Context
     * @param sourceFiles 需要拷贝的文件列表
     * @param dest 文件保存路径
     * @return true 拷贝成功；false:拷贝失败
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */

    public static boolean replaceAssetsFiles(Context context, String[] sourceFiles, String dest)
            throws IOException {
        // File xml = new File(ScreensFlyConstants.PATH);

        File destDir = new File(dest);

        if (!destDir.exists()) {
            destDir.mkdirs();
        } else if (!destDir.isDirectory()) {
            return false;
        }

        String tempDest = dest;
        if (!tempDest.endsWith(File.separator)) {
            tempDest += File.separator;
        }

        // 列出所有文件,一个一个copy.
        // String[] fileNames = DLNABinder.this.context.getAssets().list("");
        for (String fileName : sourceFiles) {

            if (fileName.contains(File.separator)) {
                String relativePath = fileName.substring(0,
                        fileName.lastIndexOf(File.separator) + 1);
                if (!relativePath.startsWith(File.separator)) {
                    relativePath = File.separator + relativePath;
                }
                String folder = tempDest + relativePath;
                if (!new File(folder).exists()) {
                    new File(folder).mkdirs();
                }
            }

            File destFile = new File(tempDest + fileName);

            if (destFile.exists()) {
                continue;
            }

            InputStream inputStream = null;
            try {
                inputStream = context.getResources().getAssets().open(fileName);
                // File file = new File(ScreensFlyConstants.PATH + fileName);
                copyFile(inputStream, destFile);
            } catch (IOException e) {
                throw e;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        }
        return true;
    }

    /**
     * 将Assets 文件流保存到指定的路径
     * 
     * @param sourceInput assets文件流
     * @param destFile 文件保存路径
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */

    private static void copyFile(InputStream sourceInput, File destFile) throws IOException {
        byte[] b = new byte[1024 * 5];
        int len = -1;

        FileOutputStream output = null;
        Log.d(TAG, "copy file: to " + destFile.getCanonicalPath());
        try {
            output = new FileOutputStream(destFile);
            while ((len = sourceInput.read(b)) != -1) {
                output.write(b, 0, len);
            }
            output.flush();
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (output != null) {
                output.close();
                output = null;
            }
        }
    }

    public static String getLanguage() {
        String language = "TW";
        if (Locale.getDefault().getLanguage().equals("zh")) {
            // 因为繁简体的区分需要通过国家来确定（TW/CN）
            language = Locale.getDefault().getCountry();
        } else {
            // 例如英文：en
            language = Locale.getDefault().getLanguage();
        }
        return "&lang=" + language;
    }
}
