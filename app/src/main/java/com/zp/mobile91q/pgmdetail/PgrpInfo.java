
package com.zp.mobile91q.pgmdetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PgrpInfo {
    // 节目组id
    private int id;
    private String contentId;
    // 节目组名
    private String name;
    // 节目组Logo
    private String logo;
    // 主演
    private String[] actor;
    // 导演
    private String director;
    // 简介
    private String desc;
    // 地区代码
    private String areaCode;
    // 地区
    private String areaName;
    // 集数描述
    private String chase;
    // 品质ID
    private int qualityId;;
    // 品质代码
    private String qualityCode;
    // 品质
    private String qualityName;
    // 类型代码
    private String typeCode;
    // 类型
    private String typeName;
    // 年代
    private String year;
    private long publishTime;

    // // 视频来源，站点id
    // private int[] source;
    private String[] sourceCode;
    // 节目列表
    private PgmInfo[] pgmList;
    // 综艺分组形式
    private Map<String, PgmInfo[]> pgMap;
    // 频道
    private String channel_code;
    public PgmInfo[] getPgmList() {
        return pgmList;
    }
    @Override
    public String toString() {
        return "PgrpInfo [name=" + name + ", actor=" + Arrays.toString(actor) + ", director=" + director + ", source="
                + Arrays.toString(sourceCode) + ", PgmList=" + Arrays.toString(pgmList) + "]";
    }
    public void setPgmList(PgmInfo[] pgmList) {
        this.pgmList = pgmList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String[] getActor() {
        return actor;
    }

    public void setActor(String[] actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getQualityCode() {
        return qualityCode;
    }

    public void setQualityCode(String qualityCode) {
        this.qualityCode = qualityCode;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    // public int[] getSource() {
    // return source;
    // }
    //
    // public void setSource(int[] source) {
    // this.source = source;
    // }

    public Map<String, PgmInfo[]> getPgMap() {
        return pgMap;
    }

    public void setPgMap(Map<String, PgmInfo[]> pgMap) {
        this.pgMap = pgMap;
    }

    public String getChase() {
        return chase;
    }

    public void setChase(String chase) {
        this.chase = chase;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String[] getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String[] sourceCode) {
        this.sourceCode = sourceCode;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public static class PgmInfo {
        // 集数
        private int showNo;

        // 节目id
        private int id;

        private String pgmContentId;

        // 节目名称
        private String name;

        // 节目logo
        private String logo;

        // 节目时间
        private int playTime;

        // 节目发布时间
        private String publishTime;

        // 描述
        private String desc;

        // 有效站点id
        // private int[] source;

        private String[] sourceCode;

        // 播放地址
        private String[] playUrl;

        private int pgmIndex;

        // 内容有hd，站点code，播放地址playurl
        private ArrayList<SourceInfo> websiteInfoList = new ArrayList<SourceInfo>();

        public ArrayList<SourceInfo> getWebsiteInfoList() {
            return websiteInfoList;
        }

        public void setWebsiteInfoList(ArrayList<SourceInfo> websiteInfoList) {
            this.websiteInfoList = websiteInfoList;
        }

        public int getShowNo() {
            return showNo;
        }

        public void setShowNo(int showNo) {
            this.showNo = showNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String[] getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String[] sourceCode) {
            this.sourceCode = sourceCode;
        }

        public String[] getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String[] playUrl) {
            this.playUrl = playUrl;
        }

        @Override
        public String toString() {
            return "PgmInfo [id=" + id + ", name=" + name + ", source=" + Arrays.toString(sourceCode) + ", playUrl="
                    + Arrays.toString(playUrl) + "]";
        }

        public String getPgmContentId() {
            return pgmContentId;
        }

        public void setPgmContentId(String pgmContentId) {
            this.pgmContentId = pgmContentId;
        }

        public int getPgmIndex() {
            return pgmIndex;
        }

        public void setPgmIndex(int pgmIndex) {
            this.pgmIndex = pgmIndex;
        }

    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
