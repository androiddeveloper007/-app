
package com.zp.mobile91q.videoplayer.subtitle;

/**
 * 字幕数据类
 */
public class SubtitleData {
    private int beginTime;// 字幕开始时间

    private int endTime;// 字幕结束时间

    // .srt格式的文件,存在上下两行的情况;其他格式的只有一行,str2为""
    private String str1;// 上面一行字幕

    private String str2;// 下面一行字幕

    public SubtitleData() {
        beginTime = 0;
        endTime = 0;
        str1 = "";
        str2 = "";
    }

    /**
     * @param ms check "ms" whether is in this Data
     * @return if in return true
     */
    public boolean checkTime(int ms) {
        return (ms >= beginTime && ms <= endTime);
    }

    /**
     * 返回某个时间点的字幕数据
     * 
     * @param ms
     * @return
     */
    public String[] getData(int ms) {
        if (ms >= beginTime && ms <= endTime) {
            String[] data = new String[] {
                    str1, str2
            };
            return data;
        }
        return null;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    @Override
    public String toString() {
        return "[str1: " + str1 + " str2: " + str2 + " beginTime: " + beginTime + "]";
    }

}
