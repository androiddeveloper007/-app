
package com.zp.mobile91q.pgmdetail;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class DefinitionAction {
    private final static String TAG = "DefinitionAction";

    private String definitionUrl = "http://qqyl.mp5so.com:8090/QxEos/v2/queryDefinition";

    private static DefinitionAction definitionAction;

    private static HashMap<String, String[]> definiton = new HashMap<String, String[]>(); // 站点code,清晰度数组

    private DefinitionAction() {
        initData();
    }

    // 第一次取不到数据的话,重新取,因此该方法也要放在子线程中
    public static synchronized DefinitionAction getinstance() {
        if (definitionAction == null) {
            definitionAction = new DefinitionAction();
        } else if (definiton == null || definiton.size() == 0) {
            definitionAction.initData();
        }
        return definitionAction;
    }

    /**
     * 获取数据
     */
    private void initData() {
        parseJsonDefinition(getDefinition());
    }

    /**
     * 获取各个站点对应清晰度
     * 
     * @return
     */
    private String getDefinition() {
        HttpURLConnection httpConnection = null;
        BufferedReader bufferReader = null;
        StringBuffer result = new StringBuffer(); // 返回json串

        try {
            URL url = new URL(definitionUrl);

            httpConnection = (HttpURLConnection) url.openConnection();

            httpConnection.setRequestProperty("Content-Type", "text/json; charset=UTF-8");
            httpConnection.setRequestProperty("Accept-Language", "zh-CN");
            httpConnection.setRequestProperty("Charset", "UTF-8");
            httpConnection.setDoOutput(true);

            httpConnection.connect();

            bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "utf-8"));

            String line = "";
            while ((line = bufferReader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception e) {
            Log.e(TAG, "getDefinition1=" + e.getMessage());
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "getDefinition2=" + e.getMessage());
                }
            }
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * 通过json串转换成Map
     * 
     * @param jsonDefinition
     */
    @SuppressWarnings("rawtypes")
    private void parseJsonDefinition(String jsonDefinition) {
        if (jsonDefinition == null || jsonDefinition.trim().equals("")) {
            return;
        }

        try {
            JSONTokener parser = new JSONTokener(jsonDefinition);
            Object object = parser.nextValue();
            // 如果不是JSONObject类型直接return
            if (!(object instanceof JSONObject)) {
                Log.e(TAG, "come in");
                return;
            }
            JSONObject jsonDefi = (JSONObject) object;
            Iterator iterator = jsonDefi.keys();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    if (!(key == null || "".equals(key.trim()))) {
                        getMap(key, jsonDefi);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseJsonDefinition JSONException=" + e.getMessage());
            return;
        }
    }

    private void getMap(String websiteCode, JSONObject jsonObject) {
        String[] temp = null;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(websiteCode);
            if (jsonArray != null) {
                temp = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    temp[i] = (String) jsonArray.getString(i);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "getMap JSONException=" + e.getMessage());
            return;
        }
        definiton.put(websiteCode, temp);
    }

    public HashMap<String, String[]> getDefiniton() {
        return definiton;
    }

}
