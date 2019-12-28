package com.zp.mobile91q.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zp.mobile91q.R;
import com.zp.mobile91q.pgmdetail.PgrpInfo.PgmInfo;

import java.util.List;

/**
 * 剧集列表adapter
 */
public class TvSeriesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private PgmInfo[] pgmList;
    //声明自定义的监听接口
    private OnItemClickListener mOnItemClickListener = null;
    private List<Boolean> booleanList;
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    public TvSeriesRvAdapter(Context cxt, PgmInfo[] pgmList) {
        this.context = cxt;
        mLayoutInflater = LayoutInflater.from(context);
        this.pgmList = pgmList;
        addFooterView();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterViews.get(viewType) != null){
            return new FooterViewHolder(mFooterViews.get(viewType));
        }else {
            View view = mLayoutInflater.inflate(R.layout.filter_rv_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(position < pgmList.length) {
            ((ItemViewHolder) holder).mContentTv.setText(pgmList[position].getName());
            holder.itemView.setTag(position);
//            if(booleanList.size()>0){
//                ((ItemViewHolder) holder).mContentTv.setSelected(booleanList.get(position));
//            }
        }
    }

    @Override
    public int getItemCount() {
        return pgmList.length+mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position >= pgmList.length){
            return mFooterViews.keyAt(position - pgmList.length);
        }else{
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
        if(this.mOnItemClickListener != null) {
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

    private void setBooleanListByPos(int pos, boolean isSelected){
        if(booleanList!=null&& booleanList.size()>0){
            for(int i=0;i<booleanList.size();i++){
                booleanList.set(i, false);
            }
            booleanList.set(pos, isSelected);
        }
    }

    //底部添加一段空间
    public void addFooterView(){
        for(int i=0;i<3;i++){
            TextView footer = new TextView(context);
            mFooterViews.put(mFooterViews.size() + 200000,footer);
        }
    }
}
