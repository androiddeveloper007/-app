
package com.zp.mobile91q.pgmdetail;

public class SourceInfo {
    private String websiteCode; // 站点code

    private String websiteName; // 站点名

    private String websiteUrl; // 站点log对应的url

    private int hd; // 清晰度

    private String hdName; // 清晰度string型

    private String playUrl; // 对应的节目播放地址

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public int getHd() {
        return hd;
    }

    public void setHd(int hd) {
        this.hd = hd;
    }

    public String getHdName() {
        return hdName;
    }

    public void setHdName(String hdName) {
        this.hdName = hdName;
    }

    @Override
    public String toString() {
        return ("SourceInfo=[" + "websiteCode:" + websiteCode + " websiteName:" + websiteName + " hdName:" + hdName + "]");
    }
}
