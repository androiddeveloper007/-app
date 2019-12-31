package com.zp.mobile91q.recyclerview;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zp.mobile91q.R;

import java.util.HashMap;
import java.util.Map;

import static com.zp.mobile91q.SearchResultInfo.SearchResultItem;

public class GridAdapter extends BaseQuickAdapter<SearchResultItem> {
    private String code="default";
    Map<Integer, ImageView> map = new HashMap<>();

    public GridAdapter(Context cxt) {
        super(cxt);
    }

    public GridAdapter(Context cxt, Fragment fg) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.grid_item;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SearchResultItem item, final int position) {
        helper.getTextView(R.id.media_info_text).setText(item.getPgrpName());
        helper.getTextView(R.id.score).setText(item.getScore() + "");
        Glide.with(helper.convertView.getContext())
                .load(item.getPgrpLogo())
                .placeholder(R.drawable.epg_bg)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.e("Glide", item.getChannelCode()+" position:"+position+" onResourceReady");
                        return false;
                    }
                })
                .into(helper.getImageView(R.id.video_image));
        map.put(position,helper.getImageView(R.id.video_image));
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.getAdapterPosition()!=-1){
            Glide.clear(holder.itemView.findViewById(R.id.video_image));
        }
    }

    public void onFragmentStop() {
//        for(Map.Entry<Integer, ImageView> entry : map.entrySet()){
//            ImageView mapValue = entry.getValue();
//            Glide.clear(mapValue);
//        }
    }

    public void setCode(String str){
        this.code = str;
    }
}
