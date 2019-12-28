package com.zp.mobile91q;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zp.mobile91q.R;

public class DemoActivity extends AppCompatActivity {
boolean b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        final TextView tv = findViewById(R.id.demoBtn);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b = !b;
                tv.setSelected(!b);
            }
        });
    }

//    public void aaa(View view) {
//        TextView tv = findViewById(R.id.mContentTv);
//    }
}
