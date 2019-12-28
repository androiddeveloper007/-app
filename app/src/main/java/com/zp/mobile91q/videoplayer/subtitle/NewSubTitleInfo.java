
package com.zp.mobile91q.videoplayer.subtitle;

public class NewSubTitleInfo {
    // 界面上显示的字幕名称
    private String subTitleName;

    // 0 外部字幕 1 内部字幕
    private int subType;

    private int subIndex;

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getSubTitleName() {
        return subTitleName;
    }

    public void setSubTitleName(String subTitleName) {
        this.subTitleName = subTitleName;
    }

    public int getSubIndex() {
        return subIndex;
    }

    public void setSubIndex(int subIndex) {
        this.subIndex = subIndex;
    }

    @Override
    public String toString() {
        return "[subName: " + subTitleName + " subType: " + subType + " subIndex: " + subIndex + "]";
    }
}
