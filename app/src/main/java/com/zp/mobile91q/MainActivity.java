package com.zp.mobile91q;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.zp.mobile91q.category.SearchCondition;
import com.zp.mobile91q.fragment.EpgMainPagerAdapter;
import com.zp.mobile91q.fragment.ListFragment;
import com.zp.mobile91q.popup.PagerBottomPopup;
import com.zp.mobile91q.search.SearchActivity;
import com.zp.mobile91q.tool.BtnClickAnimTool;
import com.zp.mobile91q.widget_and_tool.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<SearchCondition> searchConditionList;
    private EpgMainPagerAdapter adapter;
    private ViewPager viewPager;
    private BasePopupView popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.epg_viewPager);

//        viewPager.setOffscreenPageLimit(5);

        adapter = new EpgMainPagerAdapter(getSupportFragmentManager());
        adapter.setTitles(getTitles());
        viewPager.setAdapter(adapter);

        //indicator
        PagerSlidingTabStrip indicator = findViewById(R.id.tabs);
        indicator.setViewPager(viewPager);

        initSearchBtn();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                popupView = new XPopup.Builder(MainActivity.this)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PagerBottomPopup(MainActivity.this, searchConditionList));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initSearchBtn() {
        BtnClickAnimTool.setBtnClkAnim(findViewById(R.id.searchBtn));
        BtnClickAnimTool.setBtnClkAnim(findViewById(R.id.filterBtn));
    }

    private ArrayList<String> getTitles() {
        ArrayList<String> titles = new ArrayList<String>();
        titles.add(getString(R.string.searchs_frament_movie));
        titles.add(getString(R.string.searchs_frament_tvSeries));
        titles.add(getString(R.string.searchs_frament_varietyShow));
        titles.add(getString(R.string.searchs_frament_cartoon));
        titles.add(getString(R.string.searchs_frament_documentary));
        return titles;
    }

    public void filterClk(View view) {
        if(searchConditionList!=null&&searchConditionList.size()>0){
            if(popupView == null){
                popupView = new XPopup.Builder(view.getContext())
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PagerBottomPopup(MainActivity.this, searchConditionList));
            }
            popupView.show();
        }
    }

    public void searchClk(View view) {
        startActivity(new Intent(MainActivity.this, SearchActivity.class));
    }

    public void refreshFragmentFilter(int position, final String key, final String value) {
        final ListFragment fg = adapter.fgList.get(viewPager.getCurrentItem());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fg.filterSearch(key,value);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.fgList = null;
        adapter = null;
    }
}
