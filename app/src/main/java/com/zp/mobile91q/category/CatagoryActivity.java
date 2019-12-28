package com.zp.mobile91q.category;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zp.mobile91q.R;

import java.util.List;

public class CatagoryActivity extends AppCompatActivity {

    private String categoryICode = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MetaDataAction mMetaDataAction = MetaDataAction.getinstance();
//                final List<SearchCondition> searchConditionList = mMetaDataAction
//                        .getSearchCondition(categoryICode);
//                if(searchConditionList!=null&& searchConditionList.size()>0) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            TextView tv = findViewById(R.id.tv);
//                            tv.setText(searchConditionList.toString());
//                        }
//                    });
//                }

                final List<QuickSearchItem> quickSearchConditionItem = mMetaDataAction.getQuickSearchMap().get(
                        categoryICode);
                if(quickSearchConditionItem!=null&& quickSearchConditionItem.size()>0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        }).start();


    }

}
