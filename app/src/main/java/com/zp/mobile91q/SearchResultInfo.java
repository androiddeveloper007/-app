
package com.zp.mobile91q;

import java.util.ArrayList;
import java.util.List;

public class SearchResultInfo {
    private int totalCount;

    private List<SearchResultItem> itmeList = new ArrayList<SearchResultItem>();
    
    private List<DiscoverySearchItem> meiItemlList = new ArrayList<DiscoverySearchItem>();
    
    private List<TitleItem> titleList = new ArrayList<TitleItem>();
    
    public List<TitleItem> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<TitleItem> titleList) {
        this.titleList = titleList;
    }

    public List<DiscoverySearchItem> getMeiItemlList() {
        return meiItemlList;
    }

    public void setMeiItemlList(List<DiscoverySearchItem> meiItemlList) {
        this.meiItemlList = meiItemlList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<SearchResultItem> getItmeList() {
        return itmeList;
    }

    public void setPgrpList(List<SearchResultItem> itmeList) {
        this.itmeList = itmeList;
    }

    public static class SearchResultItem {

        // 节目id
        private int pgrpId;

        private String contentId;

        // 节目名
        private String pgrpName;

        // 节目Logo
        private String pgrpLogo;

        // 节目质量
        private int quality;

        // 评分
        private double score;

        // 描述(更新到、全集等)
        private String desc;

        // 节目数量
        private int num;

        // 集数更新描述
        private String chase;

        // 频道Code
        private String channelCode;

        public int getPgrpId() {
            return pgrpId;
        }

        public void setPgrpId(int pgrpId) {
            this.pgrpId = pgrpId;
        }

        public String getPgrpName() {
            return pgrpName;
        }

        public void setPgrpName(String pgrpName) {
            this.pgrpName = pgrpName;
        }

        public String getPgrpLogo() {
            return pgrpLogo;
        }

        public void setPgrpLogo(String pgrpLogo) {
            this.pgrpLogo = pgrpLogo;
        }

        public int getQuality() {
            return quality;
        }

        public void setQuality(int quality) {
            this.quality = quality;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getChase() {
            return chase;
        }

        public void setChase(String chase) {
            this.chase = chase;
        }

        public String getChannelCode() {
            return channelCode;
        }

        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        @Override
        public String toString() {
            return "SearchResultItem{" +
                    "pgrpId=" + pgrpId +
                    ", contentId='" + contentId + '\'' +
                    ", pgrpName='" + pgrpName + '\'' +
                    ", pgrpLogo='" + pgrpLogo + '\'' +
                    ", quality=" + quality +
                    ", score=" + score +
                    ", desc='" + desc + '\'' +
                    ", num=" + num +
                    ", chase='" + chase + '\'' +
                    ", channelCode='" + channelCode + '\'' +
                    '}';
        }
    }
    
    /**
     * 搜索美拍节目信息
     *
     */
    public static class DiscoverySearchItem extends SearchResultItem {
        /**
         * 美拍节目组id
         */
        private int meiId;
        
        /**
         * 美拍节目类型
         */
        private String meiType;
        
        /**
         * 实际链接
         */
        private String pgrLink;
        
        /**
         * 美拍链接
         */
        private String meiLink;
        
        /**
         * 采集链接
         */
        private String collectLink;
        
        /**
         * 状态
         */
        private String state;
        
        /**
         * 用户id
         */
        private String userId;
        
        /**
         * 用户名称
         */
        private String userName;
        
        /**
         * 创建时间
         */
        private String createTime;
        
        /**
         * 播放时长
         */
        private String duration;
        
        /**
         * 站点
         */
        private String portal;

        public int getMeiId() {
            return meiId;
        }

        public void setMeiId(int meiId) {
            this.meiId = meiId;
        }

        public String getMeiType() {
            return meiType;
        }

        public void setMeiType(String meiType) {
            this.meiType = meiType;
        }

        public String getPgrLink() {
            return pgrLink;
        }

        public void setPgrLink(String pgrLink) {
            this.pgrLink = pgrLink;
        }

        public String getMeiLink() {
            return meiLink;
        }

        public void setMeiLink(String meiLink) {
            this.meiLink = meiLink;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPortal() {
            return portal;
        }

        public void setPortal(String portal) {
            this.portal = portal;
        }

        public String getCollectLink() {
            return collectLink;
        }

        public void setCollectLink(String collectLink) {
            this.collectLink = collectLink;
        }
    }
    
    /**
     * 发现(美拍)类型信息
     *
     */
    public static class TitleItem {
        /**
         * 中文标题
         */
        private String chName;
        
        /**
         * 英文标题
         */
        private String enName;
        
        /**
         * code：1-n
         */
        private String code;

        public String getChName() {
            return chName;
        }

        public void setChName(String chName) {
            this.chName = chName;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
