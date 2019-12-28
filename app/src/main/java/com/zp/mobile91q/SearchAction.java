
package com.zp.mobile91q;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchAction extends ServiceJson {
    /**
     * 获取节止组下的节止数，追剧使用
     * 
     * @param pgrpIds
     * @return
     */
    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("unchecked")
    public Map<Integer, Integer> getPgrpCount(int[] pgrpIds) {
        if (pgrpIds == null || pgrpIds.length == 0) {
            return null;
        }
        StringBuilder parameter = new StringBuilder("ifid=ChasePgm");
        for (int i = 0, count = pgrpIds.length; i < count; i++) {
            parameter.append("&pgrpIds=").append(pgrpIds[i]);
        }

        try {
            JSONObject jsonObject = getJSONObject(parameter.toString());
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                if (content != null) {
                    Iterator<String> item = content.keys();
                    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                    while (item.hasNext()) {
                        String key = item.next();
                        map.put(Integer.parseInt(key), content.optInt(key));
                    }
                    return map;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取频道下面的节目列表
     * 
     * @param channalCode 频道code
     * @param pageCount 每页数量
     * @param pageIndex 当前页码
     * @return
     */
    public SearchResultInfo getChannelPgrpList(String channalCode,
            int pageCount, int pageIndex) {
        SearchParameter parameter = new SearchParameter();
        parameter.setChannel(channalCode);
        parameter.setPageCount(pageCount);
        parameter.setPageIndex(pageIndex);
        return search(parameter);
    }

    /**
     * 获取相关节目
     * 
     * @param pgrpId 节目组id
     * @param count 数量
     * @return
     */
    public SearchResultInfo getPgmCorrelation(int pgrpId, int count) {
        String parameter = "CorrelationPgm?pgrpId=" + pgrpId + "&count="
                + count;
        return search(parameter);
    }

    /**
     * 首字母搜索
     * 
     * @param key 首字母
     * @param pageCount 每页数量
     * @param pageIndex 当前页码
     * @return
     */
    public SearchResultInfo searchFirstLetter(String key, int pageCount,
            int pageIndex) {
        SearchParameter parameter = new SearchParameter();
        parameter.setFirstLetter(key);
        parameter.setPageCount(pageCount);
        parameter.setPageIndex(pageIndex);
        return search(parameter);
    }

    /**
     * 首字母搜索
     * 
     * @param key 首字母
     * @param channalCode 频道CODE
     * @param pageCount 每页数量
     * @param pageIndex 当前页码
     * @return
     */
    public SearchResultInfo searchFirstLetter(String key, String channalCode,
            int pageCount, int pageIndex) {
        SearchParameter parameter = new SearchParameter();
        parameter.setFirstLetter(key);
        parameter.setChannel(channalCode);
        parameter.setPageCount(pageCount);
        parameter.setPageIndex(pageIndex);
        return search(parameter);
    }

    /**
     * 搜索
     * 
     * @param parameter url参数形式
     * @return
     */
    public SearchResultInfo search(String parameter) {
        try {
            JSONObject jsonObject = getJSONObject(parameter.toString());
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                SearchResultInfo sri = new SearchResultInfo();
                sri.setTotalCount(content.optInt("totalCount"));
                List<SearchResultInfo.SearchResultItem> itemList = new ArrayList<SearchResultInfo.SearchResultItem>();
                JSONArray jsonArray = content.optJSONArray("pgrpList");
                if (jsonArray != null) {
                    for (int i = 0, count = jsonArray.length(); i < count; i++) {
                        JSONObject jsonObj = jsonArray.optJSONObject(i);
                        SearchResultInfo.SearchResultItem pgrp = new SearchResultInfo.SearchResultItem();
                        pgrp.setDesc(jsonObj.optString("desc"));
                        pgrp.setPgrpId(jsonObj.optInt("pgrpId"));
                        pgrp.setContentId(jsonObj.optString("contentId"));
                        pgrp.setPgrpLogo(jsonObj.optString("pgrpLogo"));
                        pgrp.setPgrpName(jsonObj.optString("pgrpName"));
                        pgrp.setScore(jsonObj.optDouble("score"));
                        pgrp.setQuality(jsonObj.optInt("quality"));
                        pgrp.setNum(jsonObj.optInt("num"));
                        pgrp.setChase(jsonObj.optString("chase"));
                        pgrp.setChannelCode(jsonObj.optString("channelCode"));
                        itemList.add(pgrp);
                    }
                }
                sri.setPgrpList(itemList);
                return sri;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    public SearchResultInfo searchFromCache(String parameter) {
        return searchFromCache(ServiceJson.getServerUrl(), parameter);
    }

    public SearchResultInfo searchFromCache(String serverUrl, String parameter) {
//    	Log.e("ZZP", parameter);
        try {
            JSONObject jsonObject = ServiceJson.getJSONObjectFromCache(serverUrl + parameter);
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                SearchResultInfo sri = new SearchResultInfo();
                sri.setTotalCount(content.optInt("totalCount"));
                List<SearchResultInfo.SearchResultItem> itemList = new ArrayList<SearchResultInfo.SearchResultItem>();
                JSONArray jsonArray = content.optJSONArray("pgrpList");
                if (jsonArray != null) {
                    for (int i = 0, count = jsonArray.length(); i < count; i++) {
                        JSONObject jsonObj = jsonArray.optJSONObject(i);
                        SearchResultInfo.SearchResultItem pgrp = new SearchResultInfo.SearchResultItem();
                        pgrp.setDesc(jsonObj.optString("desc"));
                        pgrp.setPgrpId(jsonObj.optInt("pgrpId"));
                        pgrp.setPgrpLogo(jsonObj.optString("pgrpLogo"));
                        pgrp.setPgrpName(jsonObj.optString("pgrpName"));
                        pgrp.setScore(jsonObj.optDouble("score"));
                        pgrp.setQuality(jsonObj.optInt("quality"));
                        pgrp.setNum(jsonObj.optInt("num"));
                        pgrp.setContentId(jsonObj.optString("contentId"));
                        pgrp.setChase(jsonObj.optString("chase"));
                        pgrp.setChannelCode(jsonObj.optString("channelCode"));
                        itemList.add(pgrp);
                    }
                }
                sri.setPgrpList(itemList);
                return sri;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索发现(美拍)
     * 
     * @param parameter
     * @return
     */
    public SearchResultInfo searchDiscoveryFromCache(String parameter) {
        return searchDiscoveryFromCache(ServiceJson.getDiscoveryUrl(), parameter);
    }

    public SearchResultInfo searchDiscoveryFromCache(String serverUrl, String parameter) {
        try {
            JSONObject jsonObject = ServiceJson.getJSONObjectFromCache(serverUrl + parameter);
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                SearchResultInfo sri = new SearchResultInfo();
                sri.setTotalCount(content.optInt("totalCount"));
                List<SearchResultInfo.DiscoverySearchItem> itemList = new ArrayList<SearchResultInfo.DiscoverySearchItem>();
                JSONArray jsonArray = content.optJSONArray("videoList");
                if (jsonArray != null) {
                    for (int i = 0, count = jsonArray.length(); i < count; i++) {
                        JSONObject jsonObj = jsonArray.optJSONObject(i);
                        SearchResultInfo.DiscoverySearchItem item = new SearchResultInfo.DiscoverySearchItem();

                        // 视频id
                        item.setContentId(jsonObj.optString("id"));
                        // 数据库id
                        try {
                            item.setMeiId(Integer.parseInt(jsonObj.optString("pgr_id")));
                        } catch (Exception e) {
                            Log.e("SearchAction", e.getMessage());
                        }
                        // 用户名
                        item.setUserName(jsonObj.optString("user_name"));
                        // 采集链接
                        item.setCollectLink(jsonObj.optString("meipai_url"));
                        // 播放时长
                        item.setDuration(jsonObj.optString("pgr_duration"));
                        // 实际链接
                        item.setPgrLink(jsonObj.optString("pgr_link"));
                        // 站点
                        item.setPortal(jsonObj.optString("portal"));
                        // 美拍连接
                        item.setMeiLink(jsonObj.optString("pgr_url"));
                        // 上传时间
                        item.setCreateTime(jsonObj.optString("create_time"));
                        // 用户id
                        item.setUserId(jsonObj.optString("user_id"));
                        // 节目名称
                        item.setPgrpName(jsonObj.optString("pgr_name"));
                        // 节目类型
                        item.setMeiType(jsonObj.optString("type"));
                        // 节目logo
                        item.setPgrpLogo(jsonObj.optString("pgr_logo"));

                        itemList.add(item);
                    }
                }
                sri.setMeiItemlList(itemList);
                return sri;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索发现(美拍)Title
     * 
     * @param parameter
     * @return
     */
    public SearchResultInfo searchDiscoveryTitleFromCache(String parameter) {
        return searchDiscoveryTitleFromCache(ServiceJson.getDisTitleUrl(), parameter);
    }

    public SearchResultInfo searchDiscoveryTitleFromCache(String serverUrl, String parameter) {
        try {
            JSONObject jsonObject = ServiceJson.getJSONObjectFromCache(serverUrl + parameter);
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                SearchResultInfo sri = new SearchResultInfo();
                sri.setTotalCount(content.optInt("totalCount"));
                List<SearchResultInfo.TitleItem> itemList = new ArrayList<SearchResultInfo.TitleItem>();
                JSONArray jsonArray = content.optJSONArray("typeList");
                if (jsonArray != null) {
                    for (int i = 0, count = jsonArray.length(); i < count; i++) {
                        JSONObject jsonObj = jsonArray.optJSONObject(i);
                        SearchResultInfo.TitleItem item = new SearchResultInfo.TitleItem();

                        // 中文名
                        item.setChName(jsonObj.optString("ch_name"));
                        // 英文名,实为拼音
                        item.setEnName(jsonObj.optString("eng_name"));
                        // 所属分类code：1,2,3...
                        item.setCode(jsonObj.optString("code"));

                        itemList.add(item);
                    }
                }
                sri.setTitleList(itemList);
                return sri;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 任意字符搜索
     * 
     * @param parameter
     * @return
     */
    public SearchResultInfo searchByKey(String parameter) {
        return search(ServiceJson.getServerUrl("search"), "key=" + parameter);
    }

    /**
     * 搜索
     * 
     * @param parameter
     * @return
     */
    public SearchResultInfo search(SearchParameter parameter) {
        return search(ServiceJson.getServerUrl("search"), parameter.toParameter());
    }

    /**
     * 搜索
     * 
     * @param parameter url参数形式
     * @return
     */
    public SearchResultInfo search(String serverUrl, String parameter) {
        try {
            if (parameter.startsWith("&")) {
                parameter = parameter.substring(1);
            }
            JSONObject jsonObject = ServiceJson.getJSONObject(serverUrl, parameter, false);
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                SearchResultInfo sri = new SearchResultInfo();
                sri.setTotalCount(content.optInt("totalCount"));
                List<SearchResultInfo.SearchResultItem> itemList = new ArrayList<SearchResultInfo.SearchResultItem>();
                JSONArray jsonArray = content.optJSONArray("pgrpList");
                if (jsonArray != null) {
                    for (int i = 0, count = jsonArray.length(); i < count; i++) {
                        JSONObject jsonObj = jsonArray.optJSONObject(i);
                        SearchResultInfo.SearchResultItem pgrp = new SearchResultInfo.SearchResultItem();
                        pgrp.setDesc(jsonObj.optString("desc"));
                        pgrp.setPgrpId(jsonObj.optInt("pgrpId"));
                        pgrp.setContentId(jsonObj.optString("contentId"));
                        pgrp.setPgrpLogo(jsonObj.optString("pgrpLogo"));
                        pgrp.setPgrpName(jsonObj.optString("pgrpName"));
                        pgrp.setScore(jsonObj.optDouble("score"));
                        pgrp.setQuality(jsonObj.optInt("quality"));
                        pgrp.setNum(jsonObj.optInt("num"));
                        pgrp.setChase(jsonObj.optString("chase"));
                        pgrp.setChannelCode(jsonObj.optString("channelCode"));
                        itemList.add(pgrp);
                    }
                }
                sri.setPgrpList(itemList);
                return sri;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class SearchParameter {
        // 节目首字母
        private String firstLetter;

        // 节目名称
        private String pgmName;

        // 主演
        private String actor;

        // 导演
        private String director;

        // 频道：电影、电视剧等
        private String channel;

        // 类型：动作、爱情、喜剧、恐怖、科幻等
        private String channelType;

        // 地区
        private String area;

        // 年份
        private String year;

        // 清晰度
        private String quality;

        // 站点来源
        private String webSiteCode;

        // 排序
        private String order;

        // 热度 TODO
        private String hot;

        // 页数
        private int pageIndex = 1;

        // 每页数量
        private int pageCount = 120;

        public String toParameter() {
            StringBuilder parameter = new StringBuilder();
            parameter.append("ifid=search");
            parameter
                    .append(getParameter("&firstLetter=", encode(firstLetter)));
            parameter.append(getParameter("&pgmName=", encode(pgmName)));
            parameter.append(getParameter("&actor=", encode(actor)));
            parameter.append(getParameter("&director=", encode(director)));
            parameter.append(getParameter("&channels=", encode(channel)));
            parameter.append(getParameter("&types=", encode(channelType)));
            parameter.append(getParameter("&area=", encode(area)));
            parameter
                    .append(getParameter("&webSiteCode=", encode(webSiteCode)));
            parameter.append(getParameter("&year=", encode(year)));
            parameter.append(getParameter("&quality=", quality));
            parameter.append(getParameter("&hot=", hot));
            parameter.append("&pageIndex=").append(pageIndex);
            parameter.append("&pageCount=").append(pageCount);
            parameter.append(getParameter("&order=", order));
            return parameter.toString();
        }

        private String encode(String str) {
            if (str != null) {
                try {
                    return URLEncoder.encode(str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            }
            return str;
        }

        private String getParameter(String name, String value) {
            if (value != null && value.length() > 0) {
                return name + value;
            }
            return "";
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getPgmName() {
            return pgmName;
        }

        public void setPgmName(String pgmName) {
            this.pgmName = pgmName;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getWebSiteCode() {
            return webSiteCode;
        }

        public void setWebSiteCode(String webSiteCode) {
            this.webSiteCode = webSiteCode;
        }

    }
}
