
package com.zp.mobile91q;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import scifly.datacache.DataCacheManager;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public abstract class ServiceJson {
    private static final String TAG = "ServiceJson";

    /**
     * 获取服务端json
     * 
     * @return
     * @throws IOException
     */
    public static String getJson(String parameter) throws IOException {
        return getJson(parameter, false);
    }

    /**
     * 获取服务端json
     * 
     * @param isPost http post请求
     * @return
     * @throws IOException
     */
    public static String getJson(String parameter, boolean isPost) throws IOException {
        return getJson(getServerUrl(), parameter, false);
    }

    /**
     * 获取服务端json
     * 
     * @param isPost http post请求
     * @return
     * @throws IOException
     */
    public static String getJson(String url, String parameter, boolean isPost) throws IOException, UnknownHostException {
        Log.w("ZZP", "请求地址 url: " + url + parameter);
        long time = System.currentTimeMillis();
        HttpUtil httpUtil = new HttpUtil(url, parameter);
        if (isPost) {
            httpUtil.setRequestMethod(HttpUtil.POST);//POST
        }
        String str = httpUtil.getUrlContent();
        Utils.print(TAG, "******api request time****** :" + (System.currentTimeMillis() - time) + "ms");

        return str;
    }

    /**
     * 判断是否有效的服务端返回
     * 
     * @param json
     * @return
     */
    public static boolean isEffectiveJson(String json) {
        return json != null && json.length() > 0 && json.indexOf("\"result\":0") > 0;
    }

    /**
     * 判断是否有效的服务端返回
     * 
     * @param json
     * @return
     */
    public static boolean isEffectiveJson(JSONObject json) {
        return json != null && json.optInt("result", Constants.UNKNOWN_ERROR) == Constants.RESULT_OK;
    }

    /**
     * @param parameter
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getJSONObject(String parameter) throws IOException, JSONException, UnknownHostException {
        return getJSONObject(parameter, false);
    }

    /**
     * @param parameter
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getJSONObject(String parameter, boolean isPost) throws IOException, JSONException,
            UnknownHostException {
        return getJSONObject(getServerUrl(), parameter, false);
    }

    /**
     * @param url
     * @param parameter
     * @param isPost
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getJSONObject(String url, String parameter, boolean isPost) throws IOException,
            JSONException, UnknownHostException {
        String json = getJson(url, parameter, isPost);

        if (isEffectiveJson(json)) {
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            return jsonObject;
        } else {
            Log.d(TAG, "!!error json :: " + json);
        }
        return null;
    }

    /**
     * 通过缓存获取数据
     * 
     * @param url
     * @return
     */
    public static JSONObject getJSONObjectFromCache(String url) {
        Utils.print(TAG, "Get JSON:" + url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .needCheck(true)
                .build();
        Log.d(TAG, "DataCacheManager.getInstance().isInited() " + DataCacheManager.getInstance().isInited());
        Object o = DataCacheManager.getInstance().loadCacheSync(DataCacheManager.DATA_CACHE_TYPE_TXT, url, null,
                options);
        try {
            String json = String.valueOf(o);
            if (isEffectiveJson(json)) {
                JSONTokener jsonTokener = new JSONTokener(json);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                return jsonObject;
            } else {
                Log.d(TAG, "!!error json :=: " + json);
                DataCacheManager.getInstance().clearMemoryCache(url);
                DataCacheManager.getInstance().clearDiskCache(url);
                // 兼容读取磁盘缓存异常后，切换为从网络加载数据
                if (o == null) {
                    
                }
                Log.d(TAG, "json not go JsonFileCache");
                JSONObject jsonObject = ServiceJson.getJSONObject(url, "", false);
                return jsonObject;

            }
        } catch (Exception e) {
            Log.e(TAG, "[Error] cast fail:" + e.getMessage());
        }
        return null;
    }

    public static String getServerUrl() {
        return Constants.epgServiceUrl;
    }

    public static String getServerUrl(String functionName) {
        return Constants.epgServiceUrl + functionName + "?";
    }

    /**
     * 获取发现接口地址
     */
    public static String getDiscoveryUrl() {
        return Constants.discoveryUrl;
    }

    /**
     * 获取发现类型地址
     */
    public static String getDisTitleUrl() {
        return Constants.discoveryTitleUrl;
    }

}
