package com.zp.mobile91q.tool;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class BtnClickAnimTool {
    public static void setBtnClkAnim(View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPivotX(v.getWidth() / 2);
                v.setPivotY(v.getHeight() / 2);

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    v.animate()
                            .scaleX(1.3f)
                            .scaleY(1.3f)
                            .setInterpolator(new BounceInterpolator())
                            .setDuration(300)
                            .start();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getAction() == MotionEvent.ACTION_UP){
                    v.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setInterpolator(new BounceInterpolator())
                            .setDuration(300)
                            .start();
                }
                return false;
            }
        });
    }
}
