
package com.zp.mobile91q.videoplayer.subtitle;

import java.util.ArrayList;

/**
 * @author jay.jiang
 * @desciption 公用的外部字幕解析类,主要为了3128平台,其他平台底层有接口
 * @date 2016.7.18
 */
public abstract class AbstractParser {
    // 字幕数据列表
    protected ArrayList<SubtitleData> subtitleDataList = new ArrayList<SubtitleData>();

    abstract AbstractParser parse(String filePath);

    /**
     * @param ms 传入此时播放的时间点
     * @return 外部字幕字符串 (.srt格式的存在上下两行的情况)
     */
    public String[] getData(int ms) {
        if (subtitleDataList != null && subtitleDataList.size() > 0) {
            for (int i = 0; i < subtitleDataList.size(); i++) {
                SubtitleData data = subtitleDataList.get(i);
                if (data != null && data.checkTime(ms)) {
                    return data.getData(ms);
                }
            }
        }
        return null;
    }
}
