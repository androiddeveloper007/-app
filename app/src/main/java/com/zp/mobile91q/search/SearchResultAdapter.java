
package com.zp.mobile91q.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zp.mobile91q.R;

public class SearchResultAdapter extends BaseAdapter {

    private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    private LayoutInflater mInflater;

    public SearchResultAdapter(Activity context, List<HashMap<String, String>> list) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    public class GridHolder {
        public TextView channel;

        public TextView name;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        if (list != null && list.size() > arg0 && arg0 >= 0) {
            return list.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        GridHolder holder;
        convertView = mInflater.inflate(R.layout.epg_search_list, null);
        holder = new GridHolder();
        holder.channel = (TextView) convertView.findViewById(R.id.epg_search_resulchannel);
        holder.name = (TextView) convertView.findViewById(R.id.epg_search_resulname);
        convertView.setTag(holder);
        if (list != null && list.size() > index && index >= 0) {
            HashMap<String, String> map = list.get(index);
            if (map != null) {
                holder.channel.setText(map.get("channelName"));
                holder.name.setText(map.get("pgmName"));
            }
        }
        return convertView;
    }

}
