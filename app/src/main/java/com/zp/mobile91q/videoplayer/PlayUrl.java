
package com.zp.mobile91q.videoplayer;

public interface PlayUrl {

    /**
     * 获取节目点播url
     * 
     * @param pgmCode 节目Code
     * @param sourceCode 站点ID
     * @return
     */
    public String getDemand(String pgmCode, String sourceCode);

    /**
     * 获取直播url
     * 
     * @param channelCode 频道Code
     * @return
     */
    public String getLive(String channelCode);

    /**
     * 获取直播url
     * 
     * @param channelCode 频道Code
     * @param sourceCode 站点ID
     * @return
     */
    public String getLive(String channelCode, String sourceCode);

    /**
     * 获取直播url
     * 
     * @param channelCode 频道Code
     * @param sourceCode 站点ID
     * @return
     */
    public String getRecall(String channelCode, String sourceCode);
}
