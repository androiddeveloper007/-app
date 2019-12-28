
package com.zp.mobile91q.pgmdetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import com.zp.mobile91q.ServiceJson;
import com.zp.mobile91q.category.MetaDataAction;
import com.zp.mobile91q.category.WebsiteInfo;
import com.zp.mobile91q.pgmdetail.PgrpInfo.PgmInfo;

public class PgrpAction extends ServiceJson {

    private static final String TAG = "PgrpAction";

    /**
     * 获取节目详情
     * 
     * @param contentId 节目组id
     */
    public PgrpInfo getPgmDetail(boolean isCache, String contentId, String lan) {
        return getPgmDetail(isCache, contentId, false, lan);
    }

    /**
     * 获取节目详情
     *
     * @param pgrpId 节目组id
     */
    public PgrpInfo getPgmDetail(boolean isCache, int pgrpId, String lan) {
        return getPgmDetail(isCache, pgrpId, false, lan);
    }

    /**
     * 获取节目详情 按时间分组 ，使用 PgrpInfo.getPgMap() 获取时间和剧集分组内容
     * 
     * @param contentId
     * @return
     */
    public PgrpInfo getPgmDeailGroupByMonth(boolean isCache, String contentId, String lan) {
        return getPgmDetail(isCache, contentId, true, lan);
    }

    /**
     * 获取节目详情 按时间分组 ，使用 PgrpInfo.getPgMap() 获取时间和剧集分组内容
     * 
     * @param pgrpId
     * @return
     */
    public PgrpInfo getPgmDeailGroupByMonth(boolean isCache, int pgrpId, String lan) {
        return getPgmDetail(isCache, pgrpId, true, lan);
    }

    /**
     * 获取节目详情
     * 
     * @param pgrpId 节目组id
     * @param isGroup 剧集是否分组
     * @return
     */
    private PgrpInfo getPgmDetail(boolean isCache, int pgrpId, boolean isGroup, String lan) {
        PgrpInfo pgrpInfo = null;
        StringBuilder parameter = new StringBuilder("PgmDetail?pgrpId=").append(pgrpId);
        if (isGroup) {
            parameter.append("&groupby=month");
        }
        parameter.append("&lang=").append(lan);
        
        try {
            JSONObject jsonObject;
            if (isCache) {
                jsonObject = getJSONObjectFromCache(ServiceJson.getServerUrl() + parameter.toString());
            } else {
                jsonObject = getJSONObject(parameter.toString());
            }
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                if (content != null) {
                    pgrpInfo = new PgrpInfo();
                    String actor = content.optString("actor");

                    // 演员以空格分开
                    if (actor != null && actor.trim().length() > 0)
                        pgrpInfo.setActor(actor.split(" "));

                    pgrpInfo.setChase(content.optString("chase"));
                    pgrpInfo.setQualityId(content.optInt("quality"));
                    pgrpInfo.setDesc(content.optString("desc"));
                    pgrpInfo.setDirector(content.optString("director"));
                    pgrpInfo.setName(content.optString("pgrpName"));
                    pgrpInfo.setId(content.optInt("pgrpId"));
                    pgrpInfo.setContentId(content.optString("contentId"));
                    pgrpInfo.setLogo(content.optString("pgrpLogo"));

                    pgrpInfo.setAreaCode(content.optString("areaCode"));
                    pgrpInfo.setAreaName(content.optString("areaName"));
                    pgrpInfo.setQualityCode(content.optString("qualityCode"));
                    pgrpInfo.setQualityName(content.optString("qualityName"));

                    pgrpInfo.setTypeCode(content.optString("typeCode"));
                    pgrpInfo.setTypeName(content.optString("typeName"));
                    pgrpInfo.setYear(content.optString("year"));
                    pgrpInfo.setChannel_code(content.optString("channelCode"));

                    JSONArray sourceCodes = content
                            .optJSONArray("webSiteCodes");
                    if (sourceCodes != null) {
                        String[] sourceCode = new String[sourceCodes.length()];
                        for (int i = 0, count = sourceCodes.length(); i < count; i++) {
                            sourceCode[i] = sourceCodes.optString(i);
                        }
                        pgrpInfo.setSourceCode(sourceCode);
                    }

                    // 剧集处理
                    JSONArray pgmsJSONArray = content.optJSONArray("pgms");
                    if (pgmsJSONArray != null) {
                        // 分组
                        if (isGroup) {
                            Map<String, PgmInfo[]> map = new LinkedHashMap<String, PgmInfo[]>();
                            List<PgmInfo> tempPgmList = new ArrayList<PgmInfo>();
                            for (int i = 0, count = pgmsJSONArray.length(); i < count; i++) {
                                JSONObject pgmJSONObject = pgmsJSONArray
                                        .getJSONObject(i);
                                String time = pgmJSONObject.optString("time");
                                PgmInfo[] pgmList = parsePgmJson(pgmJSONObject
                                        .getJSONArray("list"), tempPgmList.size());
                                tempPgmList.addAll(Arrays.asList(pgmList));
                                map.put(time, pgmList);
                            }
                            pgrpInfo.setPgMap(map);
                            pgrpInfo.setPgmList(tempPgmList
                                    .toArray(new PgmInfo[tempPgmList.size()]));
                        } else {
                            pgrpInfo.setPgmList(parsePgmJson(pgmsJSONArray, 0));
                        }
                    } else {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pgrpInfo;
    }

    /**
     * 获取节目详情
     * @param isCache 是否缓存
     * @param contentId 节目组id
     * @param isGroup 剧集是否分组
     */
    private PgrpInfo getPgmDetail(boolean isCache, String contentId, boolean isGroup, String lan) {
        PgrpInfo pgrpInfo = null;
        StringBuilder parameter = new StringBuilder("PgmDetail?contentId=").append(contentId);
        if (isGroup) {
            parameter.append("&groupby=month");
        }
        parameter.append("&lang=").append(lan);
        try {
            JSONObject jsonObject;
            if (isCache) {
                jsonObject = getJSONObjectFromCache(ServiceJson.getServerUrl() + parameter.toString());
            } else {
                jsonObject = getJSONObject(parameter.toString());
            }
            if (jsonObject != null) {
                JSONObject content = jsonObject.getJSONObject("content");
                if (content != null) {
                    Log.w("pgrp detail",content.toString());
                    pgrpInfo = new PgrpInfo();
                    String actor = content.optString("actor");
                    // 演员以空格分开
                    if (actor != null && actor.trim().length() > 0)
                        pgrpInfo.setActor(actor.split(" "));
                    pgrpInfo.setChase(content.optString("chase"));
                    pgrpInfo.setQualityId(content.optInt("quality"));
                    pgrpInfo.setDesc(content.optString("desc"));
                    pgrpInfo.setDirector(content.optString("director"));
                    pgrpInfo.setName(content.optString("pgrpName"));
                    pgrpInfo.setId(content.optInt("pgrpId"));
                    pgrpInfo.setContentId(content.optString("contentId"));
                    pgrpInfo.setLogo(content.optString("pgrpLogo"));
                    pgrpInfo.setAreaCode(content.optString("areaCode"));
                    pgrpInfo.setAreaName(content.optString("areaName"));
                    pgrpInfo.setQualityCode(content.optString("qualityCode"));
                    pgrpInfo.setQualityName(content.optString("qualityName"));
                    pgrpInfo.setTypeCode(content.optString("typeCode"));
                    pgrpInfo.setTypeName(content.optString("typeName"));
                    pgrpInfo.setYear(content.optString("year"));
                    pgrpInfo.setChannel_code(content.optString("channelCode"));
//                    pgrpInfo.setPublishTime(Long.parseLong(content.optString("publishTime")));
                    JSONArray sourceCodes = content.optJSONArray("webSiteCodes");
                    if (sourceCodes != null) {
                        String[] sourceCode = new String[sourceCodes.length()];
                        for (int i = 0, count = sourceCodes.length(); i < count; i++) {
                            sourceCode[i] = sourceCodes.optString(i);
                        }
                        pgrpInfo.setSourceCode(sourceCode);
                    }
                    // 剧集处理
                    JSONArray pgmsJSONArray = content.optJSONArray("pgms");
                    if (pgmsJSONArray != null) {
                        // 分组
                        if (isGroup) {
                            Map<String, PgmInfo[]> map = new LinkedHashMap<String, PgmInfo[]>();
                            List<PgmInfo> tempPgmList = new ArrayList<PgmInfo>();
                            for (int i = 0, count = pgmsJSONArray.length(); i < count; i++) {
                                JSONObject pgmJSONObject = pgmsJSONArray
                                        .getJSONObject(i);
                                String time = pgmJSONObject.optString("time");
                                PgmInfo[] pgmList = parsePgmJson(pgmJSONObject
                                        .getJSONArray("list"), tempPgmList.size());
                                tempPgmList.addAll(Arrays.asList(pgmList));
                                map.put(time, pgmList);
                            }
                            pgrpInfo.setPgMap(map);
                            pgrpInfo.setPgmList(tempPgmList
                                    .toArray(new PgmInfo[tempPgmList.size()]));
                        } else {
                            pgrpInfo.setPgmList(parsePgmJson(pgmsJSONArray, 0));
                        }
                    } else {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pgrpInfo;
    }

    /**
     * 解析剧集json
     * 
     * @param pgms
     * @return
     * @throws JSONException
     */
    private PgrpInfo.PgmInfo[] parsePgmJson(JSONArray pgms, int index)
            throws JSONException {
        PgrpInfo.PgmInfo[] pgmInfos = new PgrpInfo.PgmInfo[pgms.length()];
        // 剧集
        for (int i = 0, count = pgms.length(); i < count; i++) {
            PgrpInfo.PgmInfo pgminfo = new PgrpInfo.PgmInfo();
            JSONObject pmgJsonObject = pgms.getJSONObject(i);
            pgminfo.setId(pmgJsonObject.optInt("pgmId"));
            pgminfo.setPgmContentId(pmgJsonObject.optString("pgmContentId"));
            pgminfo.setPlayTime(pmgJsonObject.optInt("playTime"));
            pgminfo.setPublishTime(pmgJsonObject.optString("publishTime"));
            pgminfo.setName(pmgJsonObject.optString("pgmName"));
            pgminfo.setLogo(pmgJsonObject.optString("pgmLogo"));
            pgminfo.setShowNo(pmgJsonObject.optInt("showNo"));
            pgminfo.setDesc(pmgJsonObject.optString("desc"));
            pgminfo.setPgmIndex(index + i);

            JSONArray pgmSourceCodes = pmgJsonObject.optJSONArray("webSiteCodes");
            if (pgmSourceCodes != null) {
                String[] sourceCode = new String[pgmSourceCodes.length()];
                String[] playUrl = new String[pgmSourceCodes.length()];
                for (int j = 0, z = pgmSourceCodes.length(); j < z; j++) {
                    JSONObject webSiteInfo = pgmSourceCodes.getJSONObject(j);
                    sourceCode[j] = webSiteInfo.optString("code");
                    playUrl[j] = webSiteInfo.optString("playurl");
                }
                pgminfo.setSourceCode(sourceCode);
                pgminfo.setPlayUrl(playUrl);
                // 扩展站点
                List<SourceInfo> list = new ArrayList<SourceInfo>();
                for (int j = 0; j < pgmSourceCodes.length(); j++) {
                    JSONObject object = (JSONObject) pgmSourceCodes.get(j);
                    SourceInfo sourceInfo = new SourceInfo();
                    sourceInfo.setWebsiteCode(object.optString("code", ""));
                    sourceInfo.setPlayUrl(object.optString("playurl", ""));
                    list.add(sourceInfo);
                }
                ArrayList<SourceInfo> resultList = flat(list);
                pgminfo.setWebsiteInfoList(resultList);
            }
            pgmInfos[i] = pgminfo;
        }
        return pgmInfos;
    }

    /**
     * 扩展站点
     * 
     * @param list 传入的未扩展之前的站点list
     * @return
     */
    private ArrayList<SourceInfo> flat(List<SourceInfo> list) {
        if (list == null || list.size() == 0) {
            return null;
        }

        int size = list.size();
        ArrayList<SourceInfo> expandList = new ArrayList<SourceInfo>();

        for (int i = 3; i > 0; i--) {
            int hd = i;
            for (int j = 0; j < size; j++) {
                String websiteCode = list.get(j).getWebsiteCode();
                String playUrl = list.get(j).getPlayUrl();
                String[] hdArray = getHd(websiteCode);

                // 返回的清晰度数组里包含清晰度,就往里面添加,否则跳出
                if (hdArray != null) {
                    if (!Arrays.asList(hdArray).contains(i + "")) {
                        continue;
                    }
                }

                SourceInfo webSiteSource = new SourceInfo();

                WebsiteInfo info = MetaDataAction.getinstance().getWebsiteInfo(websiteCode);
                if (info == null) {
                    Log.d(TAG, "info is null");
                    continue;
                }

                webSiteSource.setWebsiteCode(websiteCode);
                //agMj判断一下，用中文替换
                if(TextUtils.equals("agMj", info.getName())){
                	String temp = "阿哥美劇";
                    webSiteSource.setWebsiteName(temp);
                } else {
                    webSiteSource.setWebsiteName(info.getName());
                }
                webSiteSource.setWebsiteUrl(info.getLogo());
                webSiteSource.setPlayUrl(playUrl);
                webSiteSource.setHd(hd);
                webSiteSource.setHdName(matchHDName(hd));
                expandList.add(webSiteSource);
            }
        }
        return expandList;
    }

    /**
     * 通过站点code获取清晰度数组
     *
     * @param websiteCode
     * @return
     */
    private String[] getHd(String websiteCode) {
        if (websiteCode == null || websiteCode.trim().equals("")) {
            return null;
        }
        HashMap<String, String[]> definiton = DefinitionAction.getinstance().getDefiniton();
        return definiton.get(websiteCode);
    }

    // 通过int清晰度获取string清晰度
    private String matchHDName(int hd) {
        switch (hd) {
            case 1:
                return "标清";
            case 2:
                return "高清";
            case 3:
                return "超清";
            default:
                return "高清";
        }
    }

}
