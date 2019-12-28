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
import com.zp.mobile91q.widget_and_tool.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Create by dance, at 2019/5/5
 * 筛选弹框
 */
public class PagerBottomPopup extends BottomPopupView {
    private List<SearchCondition> list;
    private MainActivity mAct;
    private ViewPager pager;

    public PagerBottomPopup(@NonNull Context context, List<SearchCondition> quickSearchConditionItem) {
        super(context);
        this.list = quickSearchConditionItem;
        mAct = (MainActivity) context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_view_pager;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        pager = findViewById(R.id.pager);
        pager.setAdapter(new PAdapter());
        pager.setOffscreenPageLimit(5);
        //indicator
        PagerSlidingTabStrip indicator = findViewById(R.id.tabs);
        indicator.setViewPager(pager);
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
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .65f);
    }

    class PAdapter extends PagerAdapter {
        PAdapter() {
            titles.add(0, "类型");
            titles.add(1, "地区");
            titles.add(2, "年份");
            titles.add(3, "排序");
            titles.add(4, "站点");
        }

        private ArrayList<String> titles = new ArrayList<String>();

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            NestedScrollView scrollView = new NestedScrollView(container.getContext());
            LinearLayout linearLayout = new LinearLayout(container.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            scrollView.addView(linearLayout);
            RecyclerView rv = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.filter_rv, bottomPopupContainer, false);
            GridLayoutManager layoutManager = new GridLayoutManager(container.getContext(), 3,/*position == 2 ? 4 : */
                    RecyclerView.VERTICAL, false);
            final FilterRvAdapter adapter = new FilterRvAdapter(container.getContext(), list, position);
            rv.setAdapter(adapter);
            rv.setLayoutManager(layoutManager);

            linearLayout.addView(rv);
            container.addView(scrollView);

            //点击筛选
            adapter.setOnItemClickListener(new FilterRvAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(View v, final int pos) {
                    mAct.refreshFragmentFilter(position, getFieldValue(position), adapter.list.get(pos).getCode());
                    dismiss();
                }
            });
            return scrollView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private String getFieldValue(int pos) {
        switch (pos) {
            case 0:
                return "types";
            case 1:
                return "area";
            case 2:
                return "year";
            case 3:
                return "order";
            case 4:
                return "webSiteCode";
            default:
                return "";
        }
    }
}
