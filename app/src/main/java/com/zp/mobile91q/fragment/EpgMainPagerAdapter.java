
package com.zp.mobile91q.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

public class EpgMainPagerAdapter extends FragmentStatePagerAdapter {
    public List<ListFragment> fgList = new ArrayList<>();
    public EpgMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private ArrayList<String> titles = new ArrayList<String>();

    private ArrayList<String> codes = new ArrayList<String>();

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        //解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
        trans.commitAllowingStateLoss();
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle data = new Bundle();
        data.putString("code", getCode(position));
        ListFragment ft1 = new ListFragment();
        ft1.setArguments(data);
        fgList.add(ft1);
        return ft1;
    }
    

    private String getCode(int position) {
        if (codes.size() == 0)
            initCodes();
        return codes.get(position);
    }

    private void initCodes() {
        codes.add(0, "movie");
        codes.add(1, "tvSeries");
        codes.add(2, "varietyShow");
        codes.add(3, "cartoon");
        codes.add(4, "sport");
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){
            Log.d("EpgMainPagerAdapter","Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }
}
