
package com.zp.mobile91q.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zp.mobile91q.Constants;
import com.zp.mobile91q.MainActivity;
import com.zp.mobile91q.R;
import com.zp.mobile91q.SearchResultInfo.SearchResultItem;
import com.zp.mobile91q.SearchService;
import com.zp.mobile91q.category.MetaDataAction;
import com.zp.mobile91q.category.QuickSearchItem;
import com.zp.mobile91q.category.SearchCondition;
import com.zp.mobile91q.pgmdetail.PgmDetailActivity;
import com.zp.mobile91q.recyclerview.BaseQuickAdapter;
import com.zp.mobile91q.recyclerview.GridAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 列表页fragment。展示电影，电视剧，动漫，综艺，体育
 */
public class ListFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private static final String TAG = "ListFragment";
    public String channelCode;
    private boolean isPrepared = false;
    private boolean isVisible = false;
    private boolean isDestoryable = false;
    private Handler searchHandler;
    private SearchResultItem item;
    private RecyclerView mRecyclerView;
    private PtrClassicFrameLayout mPtrFrameLayout;
    private GridAdapter mAdapter;
    private SearchService service = new SearchService();//搜索请求服务端数据的action
    private SearchTask searchTask;
    private HandlerThread searchThread;
    //列表数据
    Map<String, String> con = new HashMap<String, String>();

    /*
     * onCreateView之前调用,只有当isVisibleToUser时，才去记载需要的数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            Log.e(TAG,"onPause "+channelCode);//暂停不可见fragment图片加载
            if(mAdapter!=null&&channelCode!=null){
                mAdapter.onFragmentStop();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        initView(rootView);
        lazyLoad();
        return rootView;
    }

    private void initView(View rootView) {
        // UI加载后修改isPrepared
        isPrepared = true;
        views(rootView);
    }

    private void views(View view) {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mPtrFrameLayout = view.findViewById(R.id.mPtrFrameLayout);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GridAdapter(getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrameLayout.refreshComplete();
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                item = mAdapter.getItem(position);
                String channel = item.getChannelCode();
                String contentId = item.getContentId();
                String pgrpName = item.getPgrpName();
                Intent intent = new Intent(getActivity(), PgmDetailActivity.class);
                intent.putExtra("channel", channel);
                intent.putExtra("contentId", contentId);
                intent.putExtra("pgrpName", pgrpName);
                startActivity(intent);
            }
        });
        //给rv设置监听，当滑动中。停止glide请求
        //反而造成了滑动时，代替图片显示。内存中图片没有马上显示。对性能有好处。引出一个问题recyclerview回收图片太快了。
        //是不是写的有问题
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView rv, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (getActivity() != null){
//                        Glide.with(getActivity()).resumeRequests();//恢复加载
//                    }
//                }else {
//                    if (getActivity() != null){
//                        Glide.with(getActivity()).pauseRequests();//禁止加载
//                    }
//                }
//            }
//        });
        mAdapter.setCode(channelCode);
    }

    /**
     * 可见时调用
     */
    private void onVisible() {
        lazyLoad();
    }

    private void lazyLoad() {
        // 如果组件没有准备好或这不可见，return
        if (!isPrepared || !isVisible)
            return;
        initData();
    }

    private void initData() {
        Bundle data = getArguments();
        channelCode = data.getString("code");
        // 可销毁标志位
        isDestoryable = true;

        // 这里是搜索类型的位置
        con.put(service.CON_CATEGORY_I, channelCode);
        //设置当前语言环境
        con.put("lan", "CN");
        con.put("order", "new2old");
        service.intCondition(con);
        searchThread = new HandlerThread("search Thread");
        searchThread.start();
        searchHandler = new Handler(searchThread.getLooper());
        searchTask = new SearchTask(SearchType.SEARCH_CATEGORY_II);
        searchHandler.post(searchTask);

        //获取筛选菜单数据、 TODO
        new Thread(new Runnable() {
            @Override
            public void run() {
                MetaDataAction mMetaDataAction = MetaDataAction.getinstance();
                List<SearchCondition> searchConditionList = mMetaDataAction.getSearchCondition(channelCode);
                List<QuickSearchItem> quickSearchConditionItem = mMetaDataAction.getQuickSearchMap().get(channelCode);
//                Log.e("searchConditionList",searchConditionList.toString());
//                Log.e("quickSearchConditionIt",quickSearchConditionItem.toString());
                if(searchConditionList.size()>0){
                    if(getActivity()!=null)
                    ((MainActivity) getActivity()).searchConditionList = searchConditionList;
                }
            }
        }).start();
    }

    public void filterSearch(String key, String value) {
        //rv回到顶部
        mRecyclerView.scrollToPosition(0);
        con.put(service.CON_CATEGORY_I, channelCode);
        con.put("lan", "CN");
        con.put(key, value);
        service.intCondition(con);
        searchThread = new HandlerThread("search Thread");
        searchThread.start();
        searchHandler = new Handler(searchThread.getLooper());
        searchTask = new SearchTask(SearchType.SEARCH_CATEGORY_II);
        searchHandler.post(searchTask);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isDestoryable) {
            searchHandler.removeCallbacks(searchTask);
            searchThread.quit();
            searchTask = null;
            searchThread = null;
            searchHandler = null;
        }
    }

    enum SearchType {
        SEARCH_CATEGORY_I, SEARCH_CATEGORY_I_PAGE, SEARCH_CATEGORY_II, SEARCH_CATEGORY_II_PAGE, SEARCH_MEDIA_NAME;
    }

    enum UpdateListType {
        UPDATE_REPLACE, UPDATE_APPEND;
    }

    protected class SearchTask implements Runnable {
        SearchType taskType;

        SearchTask(SearchType taskType) {
            this.taskType = taskType;
        }

        @Override
        public void run() {
            if (taskType.ordinal() == SearchType.SEARCH_CATEGORY_I.ordinal()) {
                service.searchPage();
                refreshHandler.sendEmptyMessage(Constants.MODULE_EPG_WHAT_SEARCH_CATEGORY_I);
            } else if (taskType.ordinal() == SearchType.SEARCH_CATEGORY_II.ordinal()) {
                service.searchPage();
                refreshHandler.sendEmptyMessage(Constants.MODULE_EPG_WHAT_SEARCH);
            } else if (taskType.ordinal() == SearchType.SEARCH_CATEGORY_I_PAGE.ordinal()
                    || taskType.ordinal() == SearchType.SEARCH_CATEGORY_II_PAGE.ordinal()) {
                service.searchPage();
                refreshHandler.sendEmptyMessage(Constants.MODULE_EPG_WHAT_SEARCH_PAGE);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler() {
        @Override
        public synchronized void handleMessage(Message mes) {
            switch (mes.what) {
                case Constants.MODULE_EPG_WHAT_SEARCH:// 过滤查询
                    refreshMediaList(UpdateListType.UPDATE_REPLACE);
                    break;
                case Constants.MODULE_EPG_WHAT_SEARCH_PAGE:// 翻页查询
                    refreshMediaList(UpdateListType.UPDATE_APPEND);
                    break;
            }
        }
    };

    private void refreshMediaList(UpdateListType uType) {
        List<SearchResultItem> medias = service.getPageInfo().getDatas();
        if (medias != null && medias.size() > 0) {
            if (uType.ordinal() == UpdateListType.UPDATE_REPLACE.ordinal()) {
                mAdapter.setNewData(medias);
            } else if (uType.ordinal() == UpdateListType.UPDATE_APPEND.ordinal()) {
                if(medias.size()==service.getPageInfo().pageSize){
                    mAdapter.notifyDataChangedAfterLoadMore(medias, true);
                } else {
                    mAdapter.setNewData(medias);
                }
            }
            if(medias.size()==100){
                mAdapter.openLoadMore(service.getPageInfo().pageSize, true);
            }
        } else {
            if (uType.ordinal() == UpdateListType.UPDATE_REPLACE.ordinal()) {
                mAdapter.setNewData(medias);
                View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view,
                        (ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), false);
                mAdapter.setEmptyView(true, emptyView);
            } else if (uType.ordinal() == UpdateListType.UPDATE_APPEND.ordinal()) {
                View footView = getActivity().getLayoutInflater().inflate(R.layout.view_no_more, null);
                mAdapter.addFooterView(footView);
            }
        }
//        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,
//                (ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), false);
//        mAdapter.setEmptyView(true, emptyView);
    }

    @Override
    public void onLoadMoreRequested() {
        searchTask = new SearchTask(SearchType.SEARCH_CATEGORY_I_PAGE);
        searchHandler.post(searchTask);
    }

    void log(String str){
        Log.e(TAG,str);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//    }
}
