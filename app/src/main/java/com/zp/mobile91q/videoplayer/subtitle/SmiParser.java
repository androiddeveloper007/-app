
package com.zp.mobile91q.videoplayer.subtitle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * smi格式字幕解析
 */
public class SmiParser extends AbstractParser {
    // private static final String TAG = "SmiParser";

    @Override
    AbstractParser parse(String filePath) {
        try {
            String n = "\\" + System.getProperty("line.separator");
            SubtitleData data = null;
            // smi regexp
            Pattern p = Pattern.compile(
                    "<SYNC START=(\\d+)>" + "<P>(.*?)" + n + "<SYNC START=(\\d+)>" + "<P>&nbsp;" + n,
                    Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(SubtitleUtils.file2string(filePath));
            while (m.find()) {
                data = new SubtitleData();
                data.setBeginTime(Integer.parseInt(m.group(1)));
                data.setStr1(m.group(2));// text
                data.setEndTime(Integer.parseInt(m.group(3)));
                if (data != null) {
                    subtitleDataList.add(data);
                    data = null;
                }
            }
            // Log.e(TAG, "SmiParser result: " + subtitleDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
