package com.zp.mobile91q.search;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zp.mobile91q.R;

public class SearchHistoryListAdapter extends BaseAdapter{
    
    private LayoutInflater mInflater;
    
    private String[] mHistoryList;
    
    public SearchHistoryListAdapter(Activity context, String[] mHistoryList) {
        this.mHistoryList = mHistoryList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mHistoryList != null) {
            return mHistoryList.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mHistoryList != null) {
            return mHistoryList[position];
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mHistoryList != null) {
            return position;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mHistoryList == null)
                return null;
        convertView = mInflater.inflate(R.layout.search_history_list_item, null);
        TextView text = (TextView) convertView.findViewById(R.id.history_text);
        text.setText(mHistoryList[position]);
        return convertView;
    }
    
    public void setNewData(String[] list) {
    	mHistoryList = list;
    	notifyDataSetChanged();
    }
}
