
package com.zp.mobile91q.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zp.mobile91q.ServiceJson;

public class MetaDataAction extends ServiceJson {

    private static String requestParameter = "metaData2";

    private static List<SearchCondition> searchConditionList;

    private static List<SearchCondition.Item> channelList;

    private static Map<String, List<QuickSearchItem>> quickSearchMap = new HashMap<String, List<QuickSearchItem>>();

    private static int channelShowNo;

    private static int sourceCodeShowNo;

    private static Map<String, WebsiteInfo> websiteMap;

    private static Map<String, List<SearchCondition.Item>> channelMap;

    private static Map<String, List<SearchCondition.Item>> sourceCodeMap;

    private static MetaDataAction metaDataAction;

    private MetaDataAction() {
        initData();
    }

    public static synchronized MetaDataAction getinstance() {
        if (metaDataAction == null) {
            metaDataAction = new MetaDataAction();
        } else if (searchConditionList == null
                || searchConditionList.size() == 0 || websiteMap == null
                || websiteMap.size() == 0) {
            metaDataAction.refresh();
        }

        return metaDataAction;
    }

    /**
     * 获取所有的频道列表
     * 
     * @return
     */
    public List<SearchCondition.Item> getChanneList() {
        return channelList;
    }

    /**
     * 根据频道获取相应的筛选项
     * 
     * @param channelCode
     * @return
     */
    public List<SearchCondition> getSearchCondition(String channelCode) {
        if (channelMap != null && searchConditionList != null && searchConditionList.size() > channelShowNo &&
                searchConditionList.get(channelShowNo) != null) {
            searchConditionList.get(channelShowNo).setItemList(channelMap.get(channelCode));
        }
        if (searchConditionList != null && searchConditionList.size() > sourceCodeShowNo
                && searchConditionList.get(sourceCodeShowNo) != null && sourceCodeMap != null) {
            searchConditionList.get(sourceCodeShowNo).setItemList(
                    sourceCodeMap.get(channelCode));
        }
        return searchConditionList;
    }

    /**
     * 获取来源站点Map
     * 
     * @return
     */
    public Map<String, WebsiteInfo> getWebsiteMap() {
        return websiteMap;
    }

    /**
     * 根据站点Code获取来源站点信息
     * 
     * @param websiteCode
     * @return
     */
    public WebsiteInfo getWebsiteInfo(String websiteCode) {
        if (websiteCode == null || websiteMap == null)
            return null;
        return websiteMap.get(websiteCode.trim());
    }

    /**
     * 数据刷新
     */
    public void refresh() {
        initData();
    }

    /**
     * 获取数据
     */
    private void initData() {
        try {
            JSONObject jsonObject = getJSONObject(requestParameter);
//            Log.e("ZZZ",jsonObject.toString());
            if (jsonObject != null) {
                JSONObject content = jsonObject.optJSONObject("content");
                // 搜索内容
                parseSearchConditionsJson(content
                        .optJSONObject("searchConditions"));
                // 站点列表
                parseWebsiteInfosJson(content.optJSONArray("websiteInfos"));
                parseQuickSearchJson(content.optJSONObject("quickSearchCondition"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析搜索字段
     * 
     * @param jsonObject
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    private void parseSearchConditionsJson(JSONObject jsonObject)
            throws JSONException {
//    	Log.e("ZZP",jsonObject.toString());
        if (jsonObject != null) {
            Iterator<String> keyIter = jsonObject.keys();
            searchConditionList = new ArrayList<SearchCondition>();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                JSONObject jsonobj = jsonObject.optJSONObject(key);
                String showName = jsonobj.optString("showName");
                int showNo = jsonobj.optInt("showNo");
                SearchCondition sc = new SearchCondition(showName, key, showNo);
                // 按服务端给出来順序添加到List
                searchConditionList.add(sc);
                if ("channels".equals(key)) {
                    // TODO 如后面频道解析的键对应，此方式不妥，以后需调整。
                    sc.setFieldValue("types");
                    channelShowNo = showNo;
                    channelMap = new HashMap<String, List<SearchCondition.Item>>();
                    parseChannelsJson(jsonobj, channelMap);
                } else if ("sources".equals(key)) {
                    sc.setFieldValue("webSiteCode");
                    sourceCodeShowNo = showNo;
                    sourceCodeMap = new HashMap<String, List<SearchCondition.Item>>();
                    parseChannelsJson(jsonobj, sourceCodeMap);
                } else {
                    List<SearchCondition.Item> itemList = new ArrayList<SearchCondition.Item>();
                    sc.setItemList(itemList);
                    parseItems(jsonobj, itemList, "list");
                }
            }
            // 排序
            Collections.sort(searchConditionList);
        }
    }

    /**
     * 解析QuickSearchJson字段,
     * 
     * @param jsonObject
     * @throws JSONException
     */
    private void parseQuickSearchJson(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            JSONArray openClass = jsonObject.getJSONArray("sport");
            JSONArray varietyShow = jsonObject.getJSONArray("variety");
            JSONArray cartoon = jsonObject.getJSONArray("cartoon");
            JSONArray tvSeries = jsonObject.getJSONArray("tv");
            JSONArray movie = jsonObject.getJSONArray("movie");
            quickSearchMap.put("sport", parseChannelJson(openClass));
            quickSearchMap.put("varietyShow", parseChannelJson(varietyShow));
            quickSearchMap.put("cartoon", parseChannelJson(cartoon));
            quickSearchMap.put("tvSeries", parseChannelJson(tvSeries));
            quickSearchMap.put("movie", parseChannelJson(movie));
        }
    }

    /**
     * parseChannelJson
     */
    private ArrayList<QuickSearchItem> parseChannelJson(JSONArray jsonArray) throws JSONException {
        ArrayList<QuickSearchItem> list = new ArrayList<QuickSearchItem>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                QuickSearchItem item = new QuickSearchItem();
                JSONObject object = (JSONObject) jsonArray.get(i);
                item.setName(object.getString("name"));
                item.setCode(object.optString("code"));
                item.setType(object.getString("type"));
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 解析类型 不同的频道有不同的类型
     */
    private void parseChannelsJson(JSONObject jsonObject,
            Map<String, List<SearchCondition.Item>> map) throws JSONException {
        JSONArray typeListJson = jsonObject.optJSONArray("list");
        if (typeListJson != null && typeListJson.length() > 0) {
            channelList = new ArrayList<SearchCondition.Item>();
            for (int i = 0, count = typeListJson.length(); i < count; i++) {
                JSONObject typesObj = typeListJson.getJSONObject(i);
                if (typesObj != null) {
                    String code = typesObj.optString("code");
                    String name = typesObj.optString("name");
                    String id = typesObj.optString("id");
                    SearchCondition.Item item = new SearchCondition.Item(id,
                            name, code);
                    channelList.add(item);
                    List<SearchCondition.Item> itemList = new ArrayList<SearchCondition.Item>();
                    map.put(code, itemList);
                    parseItems(typesObj, itemList, "types");
                }
            }
        }

    }

    /**
     * 解析每项的值
     */
    private void parseItems(JSONObject jsonobj,
            List<SearchCondition.Item> itemList, String nodeName)
            throws JSONException {
        // 解析值列表
        JSONArray itemListJson = jsonobj.optJSONArray(nodeName);
        if (itemList != null && itemListJson != null
                && itemListJson.length() > 0) {
            for (int i = 0, count = itemListJson.length(); i < count; i++) {
                JSONObject itemJson = itemListJson.getJSONObject(i);
                if (itemJson != null) {
                    String id = itemJson.optString("id");
                    String name = itemJson.optString("name");
                    String code = itemJson.optString("code");
                    itemList.add(new SearchCondition.Item(id, name, code));
                }
            }
        }
    }

    /**
     * web站点JSON解析
     * 
     * @throws JSONException
     */
    private void parseWebsiteInfosJson(JSONArray jsonArray)
            throws JSONException {
//    	Log.e("ZZP parseWebsiteInfosJson",jsonArray.toString());
        if (jsonArray != null && jsonArray.length() > 0) {
            websiteMap = new HashMap<String, WebsiteInfo>();
            for (int i = 0, count = jsonArray.length(); i < count; i++) {
                JSONObject itemJsonObj = jsonArray.getJSONObject(i);
                int id = itemJsonObj.optInt("websiteId");
                String name = itemJsonObj.optString("websiteName");
                String logo = itemJsonObj.optString("websiteLogo");
                String webUrl = itemJsonObj.optString("website");
                String websiteCode = itemJsonObj.optString("websiteCode");
                if (websiteCode != null)
                    websiteCode = websiteCode.trim();
                WebsiteInfo websiteInfo = new WebsiteInfo(id, name, logo,
                        webUrl, websiteCode);
                websiteMap.put(websiteInfo.getWebsiteCode(), websiteInfo);
            }
        }
    }

    public Map<String, List<QuickSearchItem>> getQuickSearchMap() {
        return quickSearchMap;
    }

}
