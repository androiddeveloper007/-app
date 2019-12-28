package com.zp.mobile91q.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zp.mobile91q.R;
import com.zp.mobile91q.category.SearchCondition;
import com.zp.mobile91q.category.SearchCondition.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * 偏方搜索列表adapter
 * created by zhuzp at 2018/05/22
 */
public class FilterRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<SearchCondition> conditionList;
    //声明自定义的监听接口
    private OnItemClickListener mOnItemClickListener = null;
    private int typeIndex;
    public List<Item> list;
    private List<Boolean> booleanList;
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    public FilterRvAdapter(Context cxt, List<SearchCondition> conditions, int type) {
        this.context = cxt;
        mLayoutInflater = LayoutInflater.from(context);
        this.conditionList = conditions;
        switch (type) {
            case 0:
                typeIndex = 0;
                addFooterView();
                break;
            case 1:
                typeIndex = 2;
                break;
            case 2:
                typeIndex = 3;
                break;
            case 3:
                typeIndex = 4;
                break;
            case 4:
                typeIndex = 5;
                addFooterView();
                break;
        }
        list = conditionList.get(typeIndex).getItemList();
        if (typeIndex == 3) {
            list.add(1, new Item("2017", "2017", "2017"));
            list.add(1, new Item("2018", "2018", "2018"));
            list.add(1, new Item("2019", "2019", "2019"));
        }
        booleanList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (type == 3) {
                booleanList.add(i, i == 1);//默认选择第二项，全部
            } else {
                booleanList.add(i, i == 0);//默认选择第一项，全部
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterViews.get(viewType) != null) {
            return new FooterViewHolder(mFooterViews.get(viewType));
        } else {
            View view = mLayoutInflater.inflate(R.layout.filter_rv_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position < list.size()) {
            ((ItemViewHolder) holder).mContentTv.setText(list.get(position).getName());
            holder.itemView.setTag(position);
            if (booleanList.size() > 0) {
                ((ItemViewHolder) holder).mContentTv.setSelected(booleanList.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.conditionList.get(typeIndex).getItemList().size() + mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= list.size()) {
            return mFooterViews.keyAt(position - list.size());
        } else {
            return super.getItemViewType(position);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mContentTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mContentTv = itemView.findViewById(R.id.mContentTv);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onClick(View view) {
        if (this.mOnItemClickListener != null) {
            mOnItemClickListener.onItemClickListener(view, (int) view.getTag());
            setBooleanListByPos((int) view.getTag(), true);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private void setBooleanListByPos(int pos, boolean isSelected) {
        if (booleanList != null && booleanList.size() > 0) {
            for (int i = 0; i < booleanList.size(); i++) {
                booleanList.set(i, false);
            }
            booleanList.set(pos, isSelected);
        }
    }

    //底部添加一段空间
    public void addFooterView() {
        for (int i = 0; i < 3; i++) {
            TextView footer = new TextView(context);
            mFooterViews.put(mFooterViews.size() + 200000, footer);
        }
    }
}
