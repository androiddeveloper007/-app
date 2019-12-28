package com.zp.mobile91q.recyclerview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zp.mobile91q.R;

import static com.zp.mobile91q.SearchResultInfo.SearchResultItem;

public class GridAdapterCompat extends BaseQuickAdapter<SearchResultItem> {

    public GridAdapterCompat(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.grid_item;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SearchResultItem item, final int position) {
        ImageView appImage = helper.getImageView(R.id.video_image);
        TextView appName = helper.getTextView(R.id.media_info_text);
        TextView score = helper.getTextView(R.id.score);
        if (item != null) {
            appName.setText(item.getPgrpName());
            score.setText(item.getScore() + "");
            /*Glide.with(appImage.getContext())
                    .load(item.getPgrpLogo())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.epg_bg)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.w("Glide", item.getChannelCode()+ " position:"+position+" onLoadFailed");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e("Glide", item.getChannelCode() +" position:"+position+" onResourceReady");
                            return false;
                        }
                    })
                    .into(appImage);*/
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
//        Log.e("onViewAttachedToWindow", "position:"+holder.getAdapterPosition());
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder.getAdapterPosition()!=-1){
//            Log.e("onViewRecycled", "position:"+holder.getAdapterPosition());
//            Glide.with(holder.itemView).clear(holder.itemView.findViewById(R.id.video_image));
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.getAdapterPosition()!=-1){
//            Log.e("onViewDetached", " position:"+holder.getAdapterPosition());

//            Glide.with(holder.itemView.findViewById(R.id.video_image)).clear(holder.itemView.findViewById(R.id.video_image));

//            Glide.with(holder.itemView).clear(holder.itemView.findViewById(R.id.video_image));
        }
    }

}
