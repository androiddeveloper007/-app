package com.zp.mobile91q.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 从左至右等距离排列；
 */
public class GridLeft2RightDecoration extends RecyclerView.ItemDecoration {
    int delta = ScreenSizeUtil.dp2px(5);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //第一行
//        if((parent.getChildAdapterPosition(view)+1)/3==0){
//            outRect.top = 0;
//        }else{
//            outRect.top = delta;
//        }
//        //第一列
//        if(parent.getChildAdapterPosition(view)%3==0){
//            outRect.left = 0;
//        }else{
//            outRect.left = delta;
//        }
        outRect.top = delta;
        outRect.bottom = 0;
        outRect.left = 0;
        outRect.right = 0;
    }
}
