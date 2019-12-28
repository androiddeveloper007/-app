
package com.zp.mobile91q.videoplayer.subtitle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ssa及ass格式字幕解析类
 */
public class SsaParser extends AbstractParser {
    // private static final String TAG = "SsaParser";

    @Override
    AbstractParser parse(String filePath) {
        try {
            String n = "\\" + System.getProperty("line.separator");
            SubtitleData data = null;
            // SSA regexp
            Pattern p = Pattern.compile(
                    "Dialogue:[^,]*,\\s*" + "(\\d):(\\d\\d):(\\d\\d).(\\d\\d)\\s*,\\s*"
                            + "(\\d):(\\d\\d):(\\d\\d).(\\d\\d)" +
                            "[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*," +
                            "(.*?)" + n
                    );

            Matcher m = p.matcher(SubtitleUtils.file2string(filePath));
            while (m.find()) {
                boolean shield = false;
                if (m.group(9).indexOf("pos") >= 0) {
                    int idx = m.group(9).indexOf("}");
                    String tmp = m.group(9).substring(idx + 1);
                    if (tmp.startsWith("m"))
                        shield = true;
                }

                if ((m.group(9).startsWith("{\\pos(")) || shield) {
                    shield = false;
                    data = new SubtitleData();
                    // start time
                    data.setBeginTime(SubtitleUtils.timeToMs(Integer.parseInt(m.group(1)),
                            Integer.parseInt(m.group(2)),
                            Integer.parseInt(m.group(3)),
                            Integer.parseInt(m.group(4))));
                    data.setEndTime(SubtitleUtils.timeToMs(Integer.parseInt(m.group(5)),
                            Integer.parseInt(m.group(6)),
                            Integer.parseInt(m.group(7)),
                            Integer.parseInt(m.group(8))));
                } else {
                    String tmp = m.group(9).replaceAll("\\<font.*?\\>", "");
                    data = new SubtitleData();
                    // start time
                    data.setBeginTime(SubtitleUtils.timeToMs(Integer.parseInt(m.group(1)),
                            Integer.parseInt(m.group(2)),
                            Integer.parseInt(m.group(3)),
                            Integer.parseInt(m.group(4))));
                    data.setEndTime(SubtitleUtils.timeToMs(Integer.parseInt(m.group(5)),
                            Integer.parseInt(m.group(6)),
                            Integer.parseInt(m.group(7)),
                            Integer.parseInt(m.group(8))));
                    data.setStr1(tmp);// text
                }
                if (data != null) {
                    subtitleDataList.add(data);
                    data = null;
                }
            }
            // Log.e(TAG, "SSAParser result: " + subtitleDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
