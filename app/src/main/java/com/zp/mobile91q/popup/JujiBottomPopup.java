package com.zp.mobile91q.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.zp.mobile91q.MainActivity;
import com.zp.mobile91q.R;
import com.zp.mobile91q.category.SearchCondition;
import com.zp.mobile91q.pgmdetail.PgmDetailActivity;
import com.zp.mobile91q.pgmdetail.PgrpInfo;
import com.zp.mobile91q.pgmdetail.PgrpInfo.PgmInfo;
import com.zp.mobile91q.widget_and_tool.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Create by dance, at 2019/5/5
 * 筛选弹框
 */
public class JujiBottomPopup extends BottomPopupView {
    private PgmInfo[] pgmList;
    private PgmDetailActivity mAct;
    private RecyclerView tvSeriesRv;

    public JujiBottomPopup(@NonNull Context context, PgmInfo[] pgmList) {
        super(context);
        this.pgmList = pgmList;
        mAct = (PgmDetailActivity) context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_tv_series;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvSeriesRv = findViewById(R.id.tvSeriesRv);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3,
                RecyclerView.VERTICAL, false);
        final TvSeriesRvAdapter adapter = new TvSeriesRvAdapter(getContext(), pgmList);
        tvSeriesRv.setAdapter(adapter);
        tvSeriesRv.setLayoutManager(layoutManager);
        //点击筛选
        adapter.setOnItemClickListener(new TvSeriesRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, final int pos) {
                mAct.setSeriesIndex(pos);//选择集数
                dismiss();
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .4f);
    }

}
