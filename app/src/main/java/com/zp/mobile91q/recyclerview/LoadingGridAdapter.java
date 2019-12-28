package com.zp.mobile91q.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zp.mobile91q.R;
import com.zp.mobile91q.loadingview.Broccoli;
import com.zp.mobile91q.loadingview.BroccoliGradientDrawable;
import com.zp.mobile91q.loadingview.PlaceholderHelper;
import com.zp.mobile91q.loadingview.PlaceholderParameter;

import java.util.HashMap;
import java.util.Map;

import static com.zp.mobile91q.SearchResultInfo.SearchResultItem;

public class LoadingGridAdapter extends BaseQuickAdapter<SearchResultItem> {
    private Map<View, Broccoli> mViewPlaceholderManager = new HashMap<>();
    public LoadingGridAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.grid_item_loading;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultItem item, int position) {
        ImageView appImage = helper.getConvertView().findViewById(R.id.media_info_image);
        TextView appName = helper.getConvertView().findViewById(R.id.media_info_text);
        Broccoli mBroccoli = new Broccoli();
        mBroccoli.addPlaceholders(PlaceholderHelper.getParameter(appImage),
                PlaceholderHelper.getParameter(appName));
        mBroccoli.show();

        if (item != null) {
//            appName.setText(item.getPgrpName());
//            Glide.with(appImage.getContext()).load(item.getPgrpLogo()).into(appImage);
        }
    }
}
