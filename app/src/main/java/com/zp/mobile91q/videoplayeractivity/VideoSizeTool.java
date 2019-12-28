package com.zp.mobile91q.videoplayeractivity;

import android.util.Log;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zp.mobile91q.videoplayer.DeviceUtil;

//调整画面比例为视频原比例，撑满全屏
public class VideoSizeTool {
    public static void setVideoRatio(boolean isFullScreenOverAll, int videoW, int videoH, SurfaceView mSurface) {
        int width;//目标宽
        int height;//目标高
        if(videoH==0||videoW==0) return;
        if (videoH < videoW) {//横向矩形
            //还要区分屏幕方向
            if(isFullScreenOverAll){
                width = DeviceUtil.heightPixels;
                height = videoH * DeviceUtil.heightPixels / videoW;
            }else{
                width = DeviceUtil.widthPixels;
                height = videoH * DeviceUtil.widthPixels / videoW;
            }
        } else {//纵向矩形
            if(isFullScreenOverAll){
                height = videoH;
                width = DeviceUtil.widthPixels * videoH / videoW;
            }else{
                height = DeviceUtil.heightPixels;
                width = DeviceUtil.heightPixels * videoW / videoH;
            }
        }
        Log.e("VideoSizeTool","width:"+width+" height:"+height);
        RelativeLayout.LayoutParams videoPlayViewHorizontal = new RelativeLayout.LayoutParams(width, height);
        videoPlayViewHorizontal.addRule(RelativeLayout.CENTER_VERTICAL);
        mSurface.setLayoutParams(videoPlayViewHorizontal);
    }

    //非全屏，重设视频宽高比
    public static void setVideoRatio(int videoW, int videoH, RelativeLayout layout) {
        int width;//目标宽
        int height;//目标高
        int layoutH = layout.getMeasuredHeight();
        int layoutW = layout.getMeasuredWidth();
        if(videoH==0||videoW==0) return;
        if (videoH < videoW) {//横向矩形
            width = layoutW;
            height = videoH * layoutW / videoW;
        } else {//纵向矩形
            height = layoutH;
            width = layoutH * videoW / videoH;
        }
        Log.e("VideoSizeTool","width:"+width+" height:"+height);
        ConstraintLayout.LayoutParams videoPlayViewHorizontal = (ConstraintLayout.LayoutParams) layout.getLayoutParams();//new ConstraintLayout.LayoutParams(width, height);
        videoPlayViewHorizontal.height = height;
        videoPlayViewHorizontal.width = width;
        layout.setLayoutParams(videoPlayViewHorizontal);
    }
}
