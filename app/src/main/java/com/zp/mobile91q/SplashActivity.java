package com.zp.mobile91q;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zp.mobile91q.SearchResultInfo.SearchResultItem;
import com.zp.mobile91q.recyclerview.LoadingGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
//        mRecyclerView.setLayoutManager(layoutManager);
//        LoadingGridAdapter mAdapter = new LoadingGridAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
//        List<SearchResultItem> list = new ArrayList<>();
//        for(int i=0;i<12;i++){
//            list.add(new SearchResultItem());
//        }
//        mAdapter.setNewData(list);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                        finish();
//                    }
//                });
//            }
//        },1000);
    }

}
