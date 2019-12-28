
package com.zp.mobile91q.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.zp.mobile91q.R;
import com.zp.mobile91q.SearchAction;
import com.zp.mobile91q.SearchResultInfo;
import com.zp.mobile91q.SearchResultInfo.SearchResultItem;
import com.zp.mobile91q.videoplayeractivity.Tools;
import com.zp.mobile91q.pgmdetail.PgmDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends FragmentActivity {

    public final static String TAG = "SearchPgmActivity";

    public static final int PAGECOUNT = Integer.MAX_VALUE;// 24;

    public String pgmName = "";
    private int type = 0;
    private String key = "";
    private ListView list;
    private TextView epgSearchResultcount;
    public static final int UPDATE_LIST = 0;
    public static final int UPDATE_SEARCH_PAR = 1;
    private SearchResultAdapter mAdapter;
    private List<HashMap<String, String>> searchResultList = new ArrayList<HashMap<String, String>>();
    private long mLastSearchTime;
    private static final long MIN_SEARCH_PERIOD = 1000L;
    private HashMap<String, String> searchConditionsMaps = new HashMap<String, String>();
    private SearchResultInfo mSearchResultInfo = new SearchResultInfo();
    private SearchAction mSearchAction = new SearchAction();
    private List<SearchResultItem> mEpgList = new ArrayList<SearchResultItem>();
    private static Map<String, String> channelMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "SearchPgmActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_layout);
        initView(getIntent());
        findView();
        channelMap.put("movie", getString(R.string.searchs_frament_movie));
        channelMap.put("tvSeries", getString(R.string.searchs_frament_tvSeries));
        channelMap.put("varietyShow", getString(R.string.searchs_frament_varietyShow));
        channelMap.put("cartoon", getString(R.string.searchs_frament_cartoon));
        initData();
    }

    private void initData() {
        type = 0;
        key = pgmName;
        long mCurrentTime = System.currentTimeMillis();
        if (mCurrentTime - mLastSearchTime < MIN_SEARCH_PERIOD)
            return;
        mLastSearchTime = mCurrentTime;
        doSearchAction(key);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "SearchPgmActivity onResume");
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "SearchPgmActivity onNewIntent");
        super.onNewIntent(intent);
        initView(intent);
    }

    private void initView(Intent intent) {
        if (intent != null)
            pgmName = intent.getStringExtra("pgmName");
        list = (ListView) findViewById(R.id.epg_search_resultlist);
        list.setOnItemClickListener(mItemClickListener);
        epgSearchResultcount = (TextView) findViewById(R.id.epg_search_resultcount);
        ImageButton back = (ImageButton) findViewById(R.id.search_back_button);
        if (back != null) {
            back.setImageResource(R.drawable.back_icon);
            back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					onBackPressed();
				}
			});
        }
    }

    private void findView() {
        //状态栏，api>20,显示statusBar
        if (Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.statusBar).setVisibility(View.VISIBLE);
        }
        ImageButton mBackButton = (ImageButton) findViewById(R.id.search_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            enterVideo(arg2);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LIST:
                    if (searchResultList != null) {
                        epgSearchResultcount.setText(getString(R.string.search_result_before)
                                + searchResultList.size()
                                + getString(R.string.search_result_after)
                                + "视频");
                        mAdapter = new SearchResultAdapter(SearchResultActivity.this, searchResultList);
                        list.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case UPDATE_SEARCH_PAR:
                    key = pgmName;
                    initData();
                    break;
            }
        }
    };


    private void doSearchAction(String mPgmName) {
        if (mPgmName == null || mPgmName.trim().equals("")) {
            Log.d(TAG, "mPgmName is empty");
            return;
        }
        // 清空搜索列表，显示加载效果页面
        epgSearchResultcount.setText("");
        searchResultList.clear();
        mAdapter = new SearchResultAdapter(SearchResultActivity.this, searchResultList);
        list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        // 加入搜索历史
        saveHistory("mSearch", mPgmName);
        try {
            mPgmName = URLEncoder.encode(mPgmName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        searchConditionsMaps.put("pgmName", mPgmName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchAction();
                if (mSearchResultInfo != null) {
                    mEpgList = mSearchResultInfo.getItmeList();
                    searchResultList.clear();
                    for (SearchResultItem item : mEpgList) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("channelName",
                                "[" + getChannelName(item.getChannelCode())
                                        + "]");
                        map.put("pgmName", item.getPgrpName());
                        searchResultList.add(map);
                    }

                    mHandler.sendEmptyMessage(UPDATE_LIST);
                }
            }
        }).start();
    }

    private void saveHistory(String field, String content) {
        SharedPreferences sp = getSharedPreferences("SearchHistory", 0);
        String searchHistory = sp.getString(field, "");
        if (!searchHistory.contains(content + ",")) {
            StringBuilder sb = new StringBuilder(searchHistory);
            sb.insert(0, content + ",");
            sp.edit().putString(field, sb.toString()).commit();
        }
    }
    @SuppressWarnings("deprecation")
    private void searchAction() {
        if (searchConditionsMaps != null) {
            String key = searchConditionsMaps.get("pgmName");
            if (Tools.isChinese(key)) {
                key = URLDecoder.decode(key);
            }
            mSearchResultInfo = mSearchAction.searchByKey(key);
        }
    }

    private String getChannelName(String channelCode) {
        return channelMap.get(channelCode);
    }

    private void enterVideo(int arg2) {
        if (mEpgList != null && mEpgList.size() > arg2 && arg2 >= 0) {
            SearchResultItem item = mEpgList.get(arg2);
            int pgrpId = item.getPgrpId();
            String contentId = item.getContentId();
            String channel = item.getChannelCode();
            String pgrpName = item.getPgrpName();
            Intent intent = new Intent(this, PgmDetailActivity.class);
            intent.putExtra("pgrpId", pgrpId);
            intent.putExtra("channel", channel);
            intent.putExtra("contentId", contentId);
            intent.putExtra("pgrpName", pgrpName);
            startActivity(intent);
        }
    }

}
