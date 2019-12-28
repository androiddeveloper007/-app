
package com.zp.mobile91q.videoplayer.subtitle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * srt格式字幕解析类
 */
public class SrtParser extends AbstractParser {
    private final static String EXPRESSION = "[0-9]+";

    private final static String EXPRESSION1 = "[0-9][0-9]:[0-5][0-9]:[0-5][0-9],[0-9][0-9][0-9] --> [0-9][0-9]:[0-5][0-9]:[0-5][0-9],[0-9][0-9][0-9]";

    /**
     * @description 解析srt字幕文件
     * @param filePath 123
     */
    @Override
    public SrtParser parse(String filePath) {
        String charset = SubtitleUtils.getCharset(filePath);// 判断文件编码格式
        String line = null;
        String startTime, endTime;
        String nowRow = "", oldRow = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(filePath)), charset));
            SubtitleData srt = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals("")) {
                    // 匹配为空行
                } else if (Pattern.matches(EXPRESSION, line)) {
                    // 匹配为标号
                    nowRow = line;
                } else if (Pattern.matches(EXPRESSION1, line)) {
                    // 匹配为时间
                    startTime = line.substring(0, 12);
                    endTime = line.substring(17, 29);
                    int start = SubtitleUtils.timeToMs(startTime) - 500;// 发现字幕会延后大概半秒，所以把字幕往前提了，同理字幕前后调整也是在这调
                    int end = SubtitleUtils.timeToMs(endTime);
                    if (srt != null) {
                        subtitleDataList.add(srt);// 把本条字幕添加到字幕集中
                        srt = null;
                    }
                    srt = new SubtitleData();
                    srt.setBeginTime(start);
                    srt.setEndTime(end);
                } else {
                    // 其他为内容
                    if (!oldRow.equals(nowRow)) {
                        byte[] b = line.getBytes();
                        String str = new String(b, "utf-8");
                        if (srt != null) {
                            // 此while处理特效字幕,特效字幕中会包含如下东西：
                            // {\fad(500,500)}{\pos(320,30)}{\fn方正粗倩简体\b1}{\bord0}{\fs20}{\c&H24EFFF&}特效&时轴：{\fs20}
                            // {\c&HFFFFFF&} 土皮
                            while (str.contains("{") && str.contains("}") && (str.indexOf("{") < str.indexOf("}"))) {
                                str = str.replace(str.substring(str.indexOf("{"), str.indexOf("}") + 1), "");
                            }
                            // 去掉黑块
                            if (!str.equalsIgnoreCase("■")) {
                                srt.setStr1(str);
                            }
                        }
                    } else {
                        byte[] b = line.getBytes();
                        String str = new String(b, "utf-8");
                        if (srt != null) {
                            // 此while处理特效字幕
                            while (str.contains("{") && str.contains("}") && (str.indexOf("{") < str.indexOf("}"))) {
                                str = str.replace(str.substring(str.indexOf("{"), str.indexOf("}") + 1), "");
                            }
                            // 去掉黑块
                            if (!str.equalsIgnoreCase("■")) {
                                srt.setStr2(str);
                            }
                        }
                    }
                    oldRow = nowRow;
                }
            }
            if (srt != null) {
                subtitleDataList.add(srt);
                srt = null;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
