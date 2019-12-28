
package com.zp.mobile91q.videoplayer;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.zp.mobile91q.Constants;
import com.zp.mobile91q.HttpUtil;
import com.zp.mobile91q.ServiceJson;

import android.util.Log;

public class PolymerizePlayUrl extends ServiceJson implements PlayUrl {
    private final static String TAG = "PolymerizePlayUrl";

    /**
     * 获取点播
     * @param pgmCode 节目id
     */
    @Override
    public String getDemand(String pgmCode, String sourceCode) {
        String parameter = "pgmId=" + pgmCode + "&webSiteCode=" + HttpUtil.encoder(sourceCode);
        return getDemandPlayUrl(parameter);
    }

    public String getDemand(String sourceUrl) {
        return getDemandPlayUrl("sourceUrl=" + HttpUtil.encoder(sourceUrl) + "&hd=1");// hd=1 最低清
    }

    public String getDemand(String sourceUrl, int hd) {// + "&hd=" + hd
        return getDemandPlayUrl("sourceUrl=" + HttpUtil.encoder(sourceUrl));// hd=1 最低清
    }

    /**
     * 获取播放链接
     */
    private String getDemandPlayUrl(String parameter) {
        try {
            JSONObject jsonObject = ServiceJson.getJSONObject(ServiceJson.getServerUrl("PgmPlay2"), parameter, false);
            if (jsonObject != null) {
                return parseDemandJson(jsonObject);
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

    /**
     * 解析点播JSON
     */
    private String parseDemandJson(JSONObject jsonObject) throws IOException, JSONException {
        JSONObject contentJsonObject = jsonObject.optJSONObject("content");
        if (contentJsonObject != null) {
            int crackType = contentJsonObject.optInt("crackType");
            String url = contentJsonObject.optString("url");
            JSONObject json = new JSONObject(contentJsonObject.optString("headers"));
            String userAgent = json.optString("User-Agent");
//            Log.e(TAG, "User-Agent " + userAgent);
            if (crackType == 0) {
//                Log.e(TAG, "parseDemandJson " + url + "sciflyku" + userAgent);
                return url + "sciflyku" + userAgent;
            } else {
                String pgmId = jsonObject.optString("pgmId");
                String webSiteCode = jsonObject.optString("webSiteCode");
//                Log.e(TAG, "parseDemandJson getSecondCrack " + url + "sciflyku" + getSecondCrack(crackType, pgmId, webSiteCode, url, userAgent));
                return getSecondCrack(crackType, pgmId, webSiteCode, url, userAgent);
            }
        }
        return null;
    }

    /**
     * 获取直播，使用缺省源
     * 
     * @param channelCode 频道Code
     */
    @Override
    public String getLive(String channelCode) {
        String parameter = "ifid=LivePlay&channelCode=" + channelCode;
        return getPlayUrl(parameter);
    }

    /**
     * 获取直播
     * 
     * @param channelCode 频道Code
     */
    @Override
    public String getLive(String channelCode, String sourceCode) {
        String parameter = "ifid=LivePlay&channelCode=" + channelCode
                + "&websiteCode=" + sourceCode;
        return getPlayUrl(parameter);
    }

    @Override
    public String getRecall(String channelCode, String sourceCode) {
        return null;
    }

    /**
     * 获取播放链接
     * 
     * @param parameter
     * @return
     */
    private String getPlayUrl(String parameter) {
        try {
            JSONObject jsonObject = getJSONObject(parameter);
            return parseJson(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析点播json
     * 
     * @param jsonObject
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private String parseJson(JSONObject jsonObject) throws IOException,
            JSONException {
        if (jsonObject != null) {
            JSONObject contentJsonObject = jsonObject.optJSONObject("content");
            if (contentJsonObject != null) {
                int crackType = contentJsonObject.optInt("crackType");
                String url = contentJsonObject.optString("url");
                if (crackType == 0) {
                    return url;
                } else {
                    String userAgent = contentJsonObject.optString("userAgent");
                    String pgmId = jsonObject.optString("pgmId");
                    String webSiteCode = jsonObject.optString("webSiteCode");
                    return getSecondCrack(crackType, pgmId, webSiteCode, url, userAgent);
                }
            }
        }
        return null;
    }

    /**
     * 二次破解，主要是為了讓對方分析客戶端最近節點
     * 
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private String getSecondCrack(int crackType, String pgmCode, String sourceCode, String url, String userAgent)
            throws IOException, JSONException {
        HttpUtil httpUtil = new HttpUtil(url);
        if (userAgent != null && userAgent.trim().length() > 0) {
            httpUtil.addHeardMap("User-Agent", userAgent);
        }
        String str = httpUtil.getUrlContent();
        StringBuilder parameter = new StringBuilder();
        parameter.append("{\"pgmId\":").append(pgmCode).append(",");
        parameter.append("\"webSiteCode\":").append(sourceCode).append(",");
        parameter.append("\"crackType\":").append(crackType).append(",");
        parameter.append("\"content\":\"").append(URLEncoder.encode(str, "utf-8")).append("\"}");
        Log.e(TAG, "getSecondCrack   crackType::" + crackType + " , url::" + url);
        // 二次破解
        JSONObject jsonObject = getJSONObject(Constants.secondCrackServiceUrl, parameter.toString(), true);
        if (jsonObject != null) {
            return parseJson(jsonObject);
        }
        return null;
    }
}
