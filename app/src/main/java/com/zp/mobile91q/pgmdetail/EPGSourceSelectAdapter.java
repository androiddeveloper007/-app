
package com.zp.mobile91q.pgmdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zp.mobile91q.R;

public class EPGSourceSelectAdapter extends BaseAdapter {
    private String[] mList;

    private Context mContext;

    private ComponentSelectListener mListener;

    public EPGSourceSelectAdapter(Context context, String[] list,
            boolean isSelect, ComponentSelectListener listener) {
        super();
        mList = list;
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext.getApplicationContext())
                    .inflate(R.layout.epg_dialog_list, null);
            holder.sourceName = (TextView) convertView
                    .findViewById(R.id.epg_dialog_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sourceName.setText(mList[position]);
        holder.sourceName.setTag(position);
        holder.sourceName.setOnClickListener(myLeftClickListener);

        return convertView;
    }

    private OnClickListener myLeftClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onTextViewSelect(Integer.valueOf(view.getTag()
                        .toString()));
            }
        }
    };

    private OnClickListener myRightClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                if (view.getTag() == null) {
                    mListener.onButtonSelect_fly(-1);
                } else {
                    mListener.onButtonSelect_fly(Integer.valueOf(view.getTag().toString()));
                }
            }
        }
    };

    private OnClickListener myLocalPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                if (view.getTag() == null) {
                    mListener.onButtonSelect_local(-1);
                } else {
                    mListener.onButtonSelect_local(Integer.valueOf(view.getTag().toString()));
                }
            }
        }
    };

    // 设备没有连接的时候
    private OnClickListener notDeviceSelectListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, mContext.getString(R.string.video_device_select), Toast.LENGTH_SHORT).show();
        }
    };

    public interface ComponentSelectListener {
        public void onButtonSelect_fly(int index);

        public void onButtonSelect_local(int index);

        public void onTextViewSelect(int index);
    }

    class ViewHolder {

        public TextView sourceName;

    }

}
