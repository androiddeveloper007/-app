
package com.zp.mobile91q.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zp.mobile91q.R;
import com.zp.mobile91q.pgmdetail.PgmDetailActivity;
import com.zp.mobile91q.videoplayer.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manley
 */
public class SearchActivity extends Activity implements OnClickListener {
    private final static String TAG = "SearchPageActivity";
    private ImageButton mBackButton;
    private ImageButton mSearchButton;
    private EditText mSearchText;
    private ListView mHistoryListView;
    private TextView mClearView;
    /**
     * 清空搜索历史上面的横线,有时需要隐藏
     */
    private View mLine;

    private SearchHistoryListAdapter mAdapter;

    private String[] historyList;

    private static final String SEARCH_FIELD = "mSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page_layout);

        initView();
        initEvent();
    }

    private void initView() {
        mLine = findViewById(R.id.search_bottom_line);
        mBackButton = (ImageButton) findViewById(R.id.search_back_button);
        mSearchButton = (ImageButton) findViewById(R.id.search_button);
        mSearchText = (EditText) findViewById(R.id.search_edit_text);
        mClearView = (TextView) findViewById(R.id.clear_history);
        mHistoryListView = (ListView) findViewById(R.id.search_history_list);

        historyList = getHistoryList();
        if (historyList == null)
            hideBottomLine();

        mAdapter = new SearchHistoryListAdapter(SearchActivity.this, historyList);
        mHistoryListView.setAdapter(mAdapter);
      //状态栏，api>20,显示statusBar
        if (Build.VERSION.SDK_INT >= 21) {
        	findViewById(R.id.statusBar).setVisibility(View.VISIBLE);
        }
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void initEvent() {
        mBackButton.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        mSearchText.setOnClickListener(this);
        mClearView.setOnClickListener(this);
        // 搜索历史列表点击事件
        mHistoryListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (historyList == null)
                    return;
                String searchText = historyList[position];
                mSearchText.setText(searchText);
                // 光标定位到最后一个字符后面
                setEditTextCursorLocation();
                doSearch();
            }
        });
    }

    /**
     * 光标定位到最后一个字符后面
     */
    public void setEditTextCursorLocation() {
        CharSequence text = mSearchText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back_button:
                finish();
                break;
            case R.id.search_button:
                doSearch();
                break;
            case R.id.clear_history:
                clearHistory();
                break;
        }
    }

    /**
     * 开始搜索
     */
    private void doSearch() {
        String filter = mSearchText.getText().toString().trim();
        if (StringUtils.isEmpty(filter)) {
            Toast.makeText(this, getString(R.string.search_key_tip), Toast.LENGTH_SHORT).show();
            return;
        }

        // Start new activity
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(MODUEL_EPG_ACTION_SEARCH_FILTER, filter);
        startActivityForResult(intent,123);
        
        //点击搜索按钮后，产生一个搜索记录。此时跳到SearchPgmActivity。所以需要在onActivityResult中刷新历史列表记录
    }

    public final static String MODUEL_EPG_ACTION_SEARCH_FILTER = "pgmName";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode==123){
    		historyList = getHistoryList();
            if(mAdapter!=null&&historyList!=null&&historyList.length>0)
            	mAdapter.setNewData(historyList);
    	}
    }

    /**
     * 直接进入搜索详情
     */
    private void doHotSearch(String searchItem) {
        // Start new activity
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(MODUEL_EPG_ACTION_SEARCH_FILTER, searchItem);
        startActivity(intent);
    }

    /**
     * 清空历史记录
     */
    private void clearHistory() {
        if (historyList == null)
            return;

        SharedPreferences sp = getSharedPreferences("SearchHistory", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        // 动态刷新
        historyList = null;
        mAdapter = new SearchHistoryListAdapter(SearchActivity.this, historyList);
        mAdapter.notifyDataSetChanged();
        mHistoryListView.setAdapter(mAdapter);
        hideBottomLine();
    }

    /**
     * 获取历史列表
     */
    private String[] getHistoryList() {
        SharedPreferences sp = getSharedPreferences("SearchHistory", 0);
        String searchHistory = sp.getString(SEARCH_FIELD, "");

        return ("".equals(searchHistory) ? null : searchHistory.split(","));
    }

    /**
     * 隐藏清空搜索历史上面的横线
     */
    private void hideBottomLine() {
        mLine.setVisibility(View.GONE);
        mClearView.setVisibility(View.GONE);
    }

    private class HotSearchAdapter extends BaseAdapter {
        List<String> hotSearchlList = new ArrayList<String>();

        private LayoutInflater mInflater;

        private Activity context;

        private TextView textView;

        public HotSearchAdapter(Activity c) {
            super();
            this.context = c;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setHotSearchlList(List<String> hotSearchlList) {
            this.hotSearchlList = hotSearchlList;
        }

        public void clearList() {
            this.hotSearchlList.clear();
        }

        @Override
        public int getCount() {
            return hotSearchlList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotSearchlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.search_hot_item, null);
                textView = (TextView) convertView.findViewById(R.id.search_hot_text);
                convertView.setTag(textView);
            } else {
                textView = (TextView) convertView.getTag();
            }

            String item = hotSearchlList.get(position);
            textView.setText(item);

            return convertView;
        }
    }
}
