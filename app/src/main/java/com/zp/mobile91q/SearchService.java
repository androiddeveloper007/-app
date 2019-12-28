
package com.zp.mobile91q;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zp.mobile91q.SearchResultInfo.SearchResultItem;

/**
 * 首页列表接口
 *
 */
public class SearchService {
    /**
     * 频道类型分类(movie,tvSeries等)
     */
    public final String CON_CATEGORY_I = "categoryI";
    /**
     * 节目类型分类(type)
     */
    public final String CON_CATEGORY_II = "categoryII";
    public final String CON_MEDIA_NAME = "mediaName";
    private final String TAG = "SearchService";
    /**
     * 查询条件
     */
    private Map<String, String> cons = new HashMap<String, String>();
    /**
     * 当前页信息
     */
    private Page pageInfo;
    /**
     * 查询页数
     */
    private int pageCount = 0;
    /**
     * 查询总记录数
     */
    private int recCount = 0;
    private Object lock = new Object();
    private SearchAction searchAction = new SearchAction();
    private String lan = "zh";

    /**
     * 初始化查询条件
     */
    public static final String types = "types";
    public static final String area = "area";
    public static final String year = "year";
    public static final String order = "order";
    public static final String webSiteCode = "webSiteCode";

    public void intCondition(Map<String, String> cons) {
        synchronized (lock) {
            this.cons = cons;
            setRecCount(0);
            setPageCount(0);
            setPageInfo(new Page());
            setLan(cons.get("lan"));
        }
    }

    public void searchPage() {
        synchronized (lock) {
            Log.d(TAG, "searchPage...");
            Log.d(TAG, "searchPage:search condition size=" + cons.size());
            if (cons.size() == 0) {
                return;
            }
            StringBuilder parameter = new StringBuilder();
            parameter.append("search?");
            Log.d(TAG, "types : " + cons.get(CON_CATEGORY_II));
            Log.d(TAG, "channels : " + cons.get(CON_CATEGORY_I));
            if (cons.get(CON_CATEGORY_I) != null) {
                parameter.append("channels=").append(cons.get(CON_CATEGORY_I));
            }
            if (cons.get(CON_CATEGORY_II) != null) {
                parameter.append("&qxsubtype=").append(cons.get(CON_CATEGORY_II).trim());
            }
            if (cons.get(types) != null) {
                parameter.append("&types=").append(cons.get(types).trim());
            }
            if (cons.get(area) != null) {
                parameter.append("&area=").append(cons.get(area).trim());
            }
            if (cons.get(year) != null) {
                parameter.append("&year=").append(cons.get(year).trim());
            }
            if (cons.get(order) != null) {
                parameter.append("&order=").append(cons.get(order).trim());
            }
            if (cons.get(webSiteCode) != null) {
                parameter.append("&webSiteCode=").append(cons.get(webSiteCode).trim());
            }
            parameter.append("&pageIndex=").append(pageInfo.getPageIndex());
            parameter.append("&pageCount=").append(pageInfo.getPageSize());
            parameter.append("&lang=").append(lan==null?"zh":lan);
            parameter.append("&feature=EOS0MUJIHERNTV00");
//            Log.e(TAG, "searchPage:parameter=" + parameter.toString());
            SearchResultInfo mSearchResultInfo = searchAction.searchFromCache(parameter.toString());
            Log.d(TAG, "mSearchResultInfo = " + mSearchResultInfo);
            if (mSearchResultInfo != null) {
                List<SearchResultItem> items = mSearchResultInfo.getItmeList();
//                Log.e(TAG, "searchPage:result size=" + items.size());
//                Log.w(TAG, "searchPage:" + items.toString());
                if (items.size() > 0) {
                    int total = mSearchResultInfo.getTotalCount();
                    setRecCount(total);
                    int pageIndex = pageInfo.getPageIndex() + 1;
                    pageInfo.setPageIndex(pageIndex);
                    loadPageData(items);
                }
            }
        }
    }

    private void loadPageData(List<SearchResultInfo.SearchResultItem> items) {
        pageInfo.setDatas(items);
    }

    public Page getPageInfo() {
        return pageInfo;
    }

    private void setPageInfo(Page pageInfo) {
        this.pageInfo = pageInfo;
    }

    public int getPageCount() {
        return pageCount;
    }

    private void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRecCount() {
        return recCount;
    }

    private void setRecCount(int recCount) {
        this.recCount = recCount;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public Map<String, String> getCons() {
    	return cons;
    }

    public class Page {
        /**
         * 翻页查询的页索引
         */
        int pageIndex = 1;
        /**
         * 翻页查询的每页记录数
         */
        public int pageSize = 100;
        List<SearchResultItem> datas = new ArrayList<SearchResultItem>();
        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageCount) {
            this.pageSize = pageCount;
        }

        public List<SearchResultItem> getDatas() {
            return datas;
        }

        public void setDatas(List<SearchResultItem> items) {
            this.datas = items;
        }
    }
}
