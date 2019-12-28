
package com.zp.mobile91q.videoplayeractivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.zp.mobile91q.videoplayer.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("DefaultLocale")
public class Tools {
    private static final String FILE_SIZE_B = "B";

    private static final String FILE_SIZE_KB = "KB";

    private static final String FILE_SIZE_MB = "MB";

    private static final String FILE_SIZE_GB = "GB";

    private static final String FILE_SIZE_TB = "TB";

    private static final String FILE_SIZE_NA = "N/A";

    private static final String TAG = "Tools";

    /**
     * will millisecond number formatting 00:00:00.
     * 
     * @param duration time value to seconds for the unit.
     * @return format for 00:00:00 forms of string or '-- : -- : -- ".
     */
    @SuppressLint("DefaultLocale")
    public static String formatDurationHMS(int duration) {
        int time = duration /* / 1000 */;
        if (time <= 0) {
            return "00:00:00";
        }
        int min = time / 60 % 60;
        int hour = time / 60 / 60;
        int second = time % 60;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    public static String formatDurationHMS(int duration, boolean islist) {
        int time = duration / 1000;
        if (time <= 0) {
            return "--:--:--";
        }
        int min = time / 60 % 60;
        int hour = time / 60 / 60;
        int second = time % 60;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    /**
     * will file size into human form, the biggest said 1023 TB. @ param size file size. @ return file size minimum unit
     * for B string.
     */
    public static String formatSize(BigInteger size) {
        // Less than
        if (size.compareTo(BigInteger.valueOf(1024)) == -1) {
            return (size.toString() + FILE_SIZE_B);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024)) == -1) {
            return (size.divide(BigInteger.valueOf(1024)).toString() + FILE_SIZE_KB);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024 * 1024)) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024)).toString() + FILE_SIZE_MB);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024 * 1024 * 1024L)) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024)).toString() + FILE_SIZE_GB);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024 * 1024 * 1024L).multiply(BigInteger.valueOf(1024))) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024 * 1024L)).toString() + FILE_SIZE_TB);
        }
        return FILE_SIZE_NA;
    }

    /*
     * 格式化时长.
     */
    public static String formatTime(int time) {
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * will millisecond number formatting 00:00. @ param duration time value to seconds for the unit. @ return format
     * for 00:00 forms of string.
     */
    public static String formatDurationMS(int duration) {
        int time = duration/* / 1000 */;
        if (time == 0 && duration > 0) {
            time = 1;
        }
        int min = time / 60;
        int second = time % 60;
        return String.format("%02d:%02d", min, second);
    }

    /**
     * 格式化时间日期
     * 
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(long time) {
        Date currentdate = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(currentdate);
    }

    public static String formatDateTime2(long time) {
        Date currentdate = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(currentdate);
    }

    public static String formatDateTime5(long time) {
        Date currentdate = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(currentdate);
    }

    /**
     * 小时:分
     * 
     * @param time
     * @return
     */
    public static String formatDateTime3(long time) {
        Date currentdate = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(currentdate);
    }

    /**
     * 分:秒 目前最大值是3599000（59：59）
     * 
     * @param time
     * @return
     */
    public static String formatDateTime4(long time) {
        Date currentdate = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(currentdate);
    }

    /**
     * 柔化效果(高斯模糊)
     * 
     * @param bmp
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[] {
                1, 2, 1, 2, 4, 2, 1, 2, 1
        };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 36; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 弹出提示信息
     * 
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 访问八宝接口
     * 
     * @throws IOException
     */
    public static String request(String userBB, String jsonString, String babaoServiceUrl) throws IOException {

        StringBuilder sb = new StringBuilder();

        HttpURLConnection httpUrl = (HttpURLConnection) new URL(babaoServiceUrl).openConnection();
        httpUrl.setDoOutput(true);
        httpUrl.setRequestMethod("POST");
        httpUrl.setRequestProperty("Accept", "json");

        httpUrl.setRequestProperty("Tcip", userBB + "_0_1235456543_0");

        // Ttag: 终端特征码 + "_" + 终端软件版本号+ "_" + 接口协议版本号
        // Ttag: BQ00T3000000STB0_0.0.3490.1_1

        String Ttag = "BQ00ADRD0000MB01_0.0.3490.1_1";

        httpUrl.setRequestProperty("Ttag", Ttag);

        httpUrl.getOutputStream().write(jsonString.getBytes());

        httpUrl.getOutputStream().flush();
        httpUrl.getOutputStream().close();
        httpUrl.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), "utf-8"));

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        httpUrl.disconnect();

        return sb.toString();

    }

    /**
     * 判断网络是否链接
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取之前软件版本号
     * 
     * @param filePathAndName "/proc/isynergyinfo/isynergyinfo.txt"
     * @return
     */
    public static String readFile(String filePathAndName) {
        String fileContent = "";
        try {
            File f = new File(filePathAndName);
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void writeFile(String filePathAndName, String fileContent) {
        try {
            File f = new File(filePathAndName);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(f), "UTF-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * judgment file exists.
     * 
     * @param file {@link File}.
     * @return when the parameters specified file exists return true, otherwise returns false.
     */
    public static boolean isFileExist(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查能否ping通TV端.
     * 
     * @param m_strForNetAddress
     * @return
     */
    public static boolean isTVConnected(String m_strForNetAddress) {
        if (m_strForNetAddress == null) {
            return false;
        }
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 " + m_strForNetAddress);// m_strForNetAddress是输入的网址或者Ip地址
            int status = p.waitFor(); // status 只能获取是否成功，无法获取更多的信息
            Log.i(TAG, "isTVConnected: " + m_strForNetAddress + " status: " + status);
            if (status == 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isToTime = false;

    public static Boolean hasTask = false;

    public static Timer timer = new Timer();

    public static TimerTask getTask() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                isToTime = false;
                hasTask = true;
            }
        };
        return task;
    }

    public static TimerTask getSecondTask() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                hasTask = false;
            }
        };
        return task;
    }

    /**
     * 判断是否需要升级
     * 
     * @param tvVer
     * @param ver
     * @return
     */
    public static boolean isNeedUpdate(String tvVer, String ver) {
        if (tvVer != null && ver != null) {
            String[] tvNum = tvVer.split(".");
            String[] num = ver.split(".");
            if (tvNum.length > 0 && num.length > 0) {
                if (Integer.valueOf(tvNum[0]) < Integer.valueOf(num[0])) {
                    return true;
                }
                if (tvNum.length > 1 && num.length > 1
                        && Integer.valueOf(tvNum[1]) < Integer.valueOf(num[1])) {
                    return true;
                }
                if (tvNum.length > 2 && num.length > 2
                        && Integer.valueOf(tvNum[2]) < Integer.valueOf(num[2])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String mUserLogoUrl = null;

    private static String mLogoPath;

    public static String getUserLogoUrl() {
        return mUserLogoUrl;
    }

    public static void setUserLogoUrl(String mUserLogoUrl) {
        Tools.mUserLogoUrl = mUserLogoUrl;
    }

    public static String getLogoPath() {
        return mLogoPath;
    }

    public static void setLogoPath(String mLogoPath) {
        Tools.mLogoPath = mLogoPath + "/logo.JPEG";
    }

    /**
     * 判断传入activityName是否在栈顶
     */
    @TargetApi(Build.VERSION_CODES.Q)
    public static boolean isCurrentActivity(Context context, String activityName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        Log.d(TAG, "activityName: " + activityName + " runningActivity: " + runningActivity);
        if (runningActivity.contains(activityName)) {
            return true;
        }
        return false;
    }

    public static Long dateString2Long(String sDt) {
        long lTimeStart = 0;
        if (StringUtils.isEmpty(sDt)) {
            return 0l;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt = sdf.parse(sDt);
            lTimeStart = dt.getTime() / 1000;
        } catch (ParseException e) {
            Log.v(TAG, "ParseException" + e.toString());
            e.printStackTrace();
        }
        return lTimeStart;
    }

    /**
     * 通过分辨率height来获取录制码流大小
     * 
     * @param height
     * @return
     */
    public static int getQualityByReso(int height) {
        int quality = 2;
        if (height < 480) {
            quality = 1;
        } else if (height < 720) {
            quality = 2;
        } else if (height < 1080) {
            quality = 3;
        } else {
            quality = 4;
        }
        return quality;
    }

    public static String getMac() {
        String mac = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                if (iF.getName() != null && iF.getName().equals("wlan0")) {
                    byte[] addr = iF.getHardwareAddress();
                    if (addr != null && addr.length > 0) {
                        StringBuilder buf = new StringBuilder();
                        for (byte b : addr) {
                            buf.append(String.format("%02X:", b));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        mac = buf.toString();
                    }
                }
            }
        } catch (SocketException e) {
            Log.d("jay", "getMac error");
        }
        return mac;
    }
}
