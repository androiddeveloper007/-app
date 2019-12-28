
package com.zp.mobile91q.videoplayer.subtitle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.text.TextUtils;
import android.util.Log;

/**
 * 字幕数据处理工具类
 * 
 * @author jay
 */
public class SubtitleUtils {
    private static final String TAG = "SubtitleUtils";

    /**
     * 时间轴转换为毫秒
     */
    public static int timeToMs(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        int mintue = Integer.parseInt(time.substring(3, 5));
        int scend = Integer.parseInt(time.substring(6, 8));
        int milli = Integer.parseInt(time.substring(9, 12));
        int msTime = (hour * 3600 + mintue * 60 + scend) * 1000 + milli;
        return msTime;
    }

    /**
     * @description to ms
     * @param hour
     * @param mintue
     * @param second
     * @param milli
     * @return ms
     */
    public static int timeToMs(int hour, int mintue, int second, int milli) {
        if (!isTimeValid(hour, mintue, second, milli)) {
            Log.e(TAG, "time is not valid and change it to valid one.");
            int addSec, addMin, addH;
            addSec = addMin = addH = 0;

            // millisecs
            addSec = milli / 1000;
            milli = milli % 1000;
            if (milli < 0) {
                milli = 1000 + milli;
                addSec--;
            }

            // sec
            second = second + addSec;
            addMin = second / 60;
            second = second % 60;
            if (second < 0) {
                second = 60 + second;
                addMin--;
            }

            // min
            mintue = mintue + addMin;
            addH = mintue / 60;
            mintue = mintue % 60;
            if (mintue < 0) {
                mintue = 60 + mintue;
                addH--;
            }

            // hour
            hour = hour + addH;
            if (hour < 0) {
                hour = 0;
            }
        }
        int msTime = (hour * 3600 + mintue * 60 + second) * 1000 + milli;
        return msTime;
    }

    /**
     * @description 传入的时间是否有效
     * @param hour
     * @param mintue
     * @param scend
     * @param milli
     * @return
     */
    public static boolean isTimeValid(int hour, int mintue, int scend, int milli) {
        if (hour < 0) {
            hour = 0;
        }
        if ((mintue < 0) || (mintue > 59)) {
            return false;
        }
        if ((scend < 0) || (scend > 59)) {
            return false;
        }
        if ((milli < 0) || (milli > 999)) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件的编码格式
     */
    public static String getCharset(String fileName) {
        String code = "UTF-8";
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(fileName));
            int p = (bis.read() << 8) + bis.read();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return code;
    }

    /**
     * 按照文件编码取文件内容
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String file2string(String filePath) throws IOException {
        Log.i(TAG, "file2string: " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + " doesn't exist.");
        }
        if (file.isDirectory()) {
            throw new FileNotFoundException(filePath + " is a directory.");
        }
        return getContents(file, getCharset(filePath));
    }

    private static String getContents(File file, String encoding) throws IOException {
        Log.e(TAG, "file: " + file + " encoding: " + encoding);
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String line = null;
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                throw ex;
            }
        }
        return contents.toString();
    }

    /**
     * @param filePath 字幕路径
     * @return 字幕类型
     */
    public static SUBTYPE getSubType(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return SUBTYPE.SUB_INVALID;
        }
        String tempPath = filePath.substring(filePath.lastIndexOf("."), filePath.length());
        SUBTYPE type = SUBTYPE.SUB_INVALID;
        if (tempPath.equals(".srt")) {
            type = SUBTYPE.SUB_SRT;
        } else if (tempPath.equals(".ssa")) {
            type = SUBTYPE.SUB_SSA;
        } else if (tempPath.equals(".ass")) {
            type = SUBTYPE.SUB_ASS;
        } else if (tempPath.equals(".smi")) {
            type = SUBTYPE.SUB_SMI;
        } else if (tempPath.equals(".txt")) {
            type = SUBTYPE.SUB_TXT;
        } else if (tempPath.equals(".idx")) {
            type = SUBTYPE.SUB_IDXSUB;
        }
        return type;
    }

    public static String checkEncoding(String fileName, String enc) {
        BufferedInputStream bis = null;
        FileInputStream fin = null;
        byte[] first3Bytes = new byte[3];
        String charset = enc;

        int idx = fileName.indexOf("INSUB");
        Log.i(TAG, "[checkEncoding]fileName:" + fileName + ",idx:" + idx);
        if (idx != -1) {
            return charset;
        }

        try {
            fin = new FileInputStream(new File(fileName));
            bis = new BufferedInputStream(fin);
            bis.mark(0);
            int r = bis.read(first3Bytes, 0, 3);
            if (r == -1) {
                if (fin != null)
                    fin.close();
                if (bis != null)
                    bis.close();
                return charset;
            }

            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF8";
            } else {
                String probe = probe_utf8_utf16(fileName, 1024);
                if (probe != "") {
                    charset = probe;
                }
            }

            if (fin != null) {
                fin.close();
            }
            if (bis != null) {
                bis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "filename: " + fileName + " charset:" + charset);
        return charset;
    }

    public static String probe_utf8_utf16(String fileName, int size) {
        char probe_find_time = 10;
        int utf8_count = 0;
        int little_utf16 = 0;
        int big_utf16 = 0;
        int i = 0;
        byte[] probeBytes = new byte[size];
        BufferedInputStream bis = null;
        FileInputStream fin = null;

        int r = 0;
        try {
            fin = new FileInputStream(new File(fileName));
            bis = new BufferedInputStream(fin);
            bis.mark(0);
            r = bis.read(probeBytes, 0, size);
            if (fin != null)
                fin.close();
            if (bis != null)
                bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (r == -1) {
            return "";
        }
        for (i = 0; i < (size - 5); i++) {
            if (probeBytes[i] == 0 && (probeBytes[i + 1] > 0x20 && probeBytes[i + 1] < 0x7d)) {
                i++;
                big_utf16++;
            } else if (probeBytes[i + 1] == 0 && (probeBytes[i] > 0x20 && probeBytes[i] < 0x7d)) {
                i++;
                little_utf16++;
            } else if (((probeBytes[i] & 0xE0) == 0xE0) && ((probeBytes[i + 1] & 0xC0) == 0x80)
                    && ((probeBytes[i + 2] & 0xC0) == 0x80)) {
                i += 2;
                utf8_count++;
                if ((((probeBytes[i + 1] & 0x80) == 0x80) && ((probeBytes[i + 1] & 0xE0) != 0xE0)) ||
                        (((probeBytes[i + 2] & 0x80) == 0x80) && ((probeBytes[i + 2] & 0xC0) != 0x80)) ||
                        (((probeBytes[i + 3] & 0x80) == 0x80) && ((probeBytes[i + 3] & 0xC0) != 0x80)))
                    return "";
            }
            if (big_utf16 >= probe_find_time) {
                return "UTF-16BE";
            } else if (little_utf16 >= probe_find_time) {
                return "UTF-16LE";
            } else if (utf8_count >= probe_find_time) {
                return "UTF-8";
            }
        }
        if (i == (size - 2)) {
            if (big_utf16 > 0) {
                return "UTF-16BE";
            } else if (little_utf16 > 0) {
                return "UTF-16LE";
            } else if (utf8_count > 0) {
                return "UTF-8";
            }
        }
        return "";
    }
}
