
package com.zp.mobile91q.videoplayer;

import com.zp.mobile91q.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class StringUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    // private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日");
        }
    };

    private static Calendar cal = Calendar.getInstance();

    private static String Week = "星期";

    /**
     * 将字符串转位日期类型
     * 
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     * 
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input.trim()))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     * 
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    // *************************************//
    /**
     * 1、 前两位固定标识“CI”，表示该字符串为内容的编码（ContentID）； 2、 第3~4位为校验码，表示除此两位之外的三十位的CRC8校验码； 3、 第5~8位为子系统编码，目前定义 0000 赛飞聚合视频 0001
     * 赛飞自编节目； 4、 第9~12位为内容类型编码，目前定义 0000 点播 0001 直播； 5、
     * 第13~16位为CRC16校验码，若第6部分为MD5，这里是第6部分原文的CRC16校验码，若第6部分不是MD5，这里是第6部分的CRC16校验码； 6、
     * 第17~32位为子系统内容标识的MD5或者子系统内容编码，不足16位的从左边开始以0补齐，若是十进制的ID，则需要转换为十六进制表示，对于聚合节目，推荐使用MD5算法以保证采集后的ID不变。
     */
    // ***************************************//

    /**
     * 获取live的房间号。
     * 
     * @param cId
     * @param defValue
     * @return
     */
    public static int contentIdToChannelID(String cId, int defValue) {

        if (!cId.startsWith("CI") && cId.length() != 32)
            return defValue;

        String s_16_32 = cId.substring(16);
        s_16_32 = s_16_32.replaceAll("^0+", "");
        if ("".equals(s_16_32)) {
            return defValue;
        }
        int s_16_32_int = Integer.valueOf(s_16_32, 16);
        return s_16_32_int;
    }

    /**
     * 获取上报流水SN
     */
    public static String md5(String string, String charsetName) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(charsetName));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String makeRandomString(int length) {
        String s = "";
        while (s.length() < length)
            s += (int) (Math.random() * 10);
        return s;
    }

    public static String encoder(String str) {
        if (!isEmpty(str)) {
            try {
                str = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return str;
    }

    /**
     * 获取一位小数
     * 
     * @return
     */
    public static String getOne(double x) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(x);
    }

    /**
     * 根据毫秒值获取时间字符串"yyyy-MM-dd HH:mm:ss"
     */
    public static String getTimeYMDHMS(long time) {
        return dateFormater.get().format(time);
    }

    /**
     * 根据毫秒值获取时间字符串"yyyy-MM-dd"
     */
    public static String getTimeString(long time) {
        return dateFormater2.get().format(time);
    }

    /**
     * 获取毫秒值
     */
    public static long getTimeLong(String time) {
        try {
            return dateFormater2.get().parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当时分
     */
    public static String getTimeHM(long time) {
        return dateFormater3.get().format(time);
    }

    /**
     * 获取包涵星期几的日期
     */
    public static String getWeek(long time) {
        String Week2 = "";
        cal.setTime(new Date(time));
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week2 = "天";
                break;
            case 2:
                Week2 = "一";
                break;
            case 3:
                Week2 = "二";
                break;
            case 4:
                Week2 = "三";
                break;
            case 5:
                Week2 = "四";
                break;
            case 6:
                Week2 = "五";
                break;
            case 7:
                Week2 = "六";
                break;
            default:
                break;
        }
        return dateFormater4.get().format(time) + Week + Week2;
    }
}
