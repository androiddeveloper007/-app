
package com.zp.mobile91q.videoplayer;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class VideoPlayerView extends SurfaceView implements OnGestureListener {
    private static final String TAG = "VideoPlayerView";
    private Context context;
    private boolean ifHorizontal = false;
    private int pd = 0;// 实际的宽度
    private int shifting = 0;// 记录偏移量
    private GestureDetector mGestureDetector;
    private VideoGestureListener mVideoGestureListener;//滑动监听

    // 自定义控件在布局中使用时必须用以下构造器
    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mGestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        Log.i(TAG, "onDown");
        shifting = 0;
        return true;
    }

    @Override
    public boolean onFling(MotionEvent motionEventDown, MotionEvent motionEventUp, float moveX, float moveY) {
//        Log.i(TAG, "onFling");
        // 竖直方向可偏移的像素最大值,判断上下滑动的竖直性，水平移动时竖直方向的偏移量最大值
        int offDistance = 150;
//        Log.d(TAG, "向下 arg0: " + motionEventDown.getAction() + "; 向上 arg1: " + motionEventUp.getAction());
        // 水平滑动最小距离判断快进、快退
        int xDistance = 300;
//        Log.d(TAG, "down x: " + motionEventDown.getX());
//        Log.d(TAG, "up x: " + motionEventUp.getX());
//        Log.d(TAG, "Y minus: " + (motionEventUp.getY() - motionEventDown.getY()));
        // 监听在横屏时才会出现，所以此处用屏幕的高度
//        Log.d(TAG, "DeviceUtil 高度 " + DeviceUtil.heightPixels);
//        Log.d(TAG, "DeviceUtil 宽度 " + DeviceUtil.widthPixels);
        if (Math.abs(motionEventDown.getY() - motionEventUp.getY()) < offDistance) {
            if (Math.abs(motionEventDown.getX() - motionEventUp.getX()) >= xDistance) {
                if (motionEventDown.getX() > motionEventUp.getX()) {
//                    Log.d(TAG, "向左移动，快退");
                    if (mVideoGestureListener != null) {
                        mVideoGestureListener.onLeftFling();
                    }
                } else {
//                    Log.d(TAG, "向右移动，快进");
                    if (mVideoGestureListener != null) {
                        mVideoGestureListener.onRightFling();
                    }
                }
            }
        }
        return true;
    }

    // 多个action_down(重按着)
    @Override
    public void onLongPress(MotionEvent arg0) {
//        Log.i(TAG, "onLongPress");
    }

    // action_down+多个action_move,缓慢移动
    @Override
    public boolean onScroll(MotionEvent motionEventDown, MotionEvent motionEventUp, float distanceX, float distanceY) {
//        Log.i(TAG, "onScroll");
        // 判断横竖屏
        ifHorizontal();
        // 上下滑动偏移的像素最小值
        int distance = 30;
        // 竖直方向可偏移的像素最大值,判断上下滑动的竖直性，水平移动时竖直方向的偏移量最大值
        int offDistance = 150;
//        Log.d(TAG, "down x: " + motionEventDown.getX());
//        Log.d(TAG, "up x: " + motionEventUp.getX());
//        Log.d(TAG, "motionEventUp.getY()： " + motionEventUp.getY());
//        Log.d(TAG, "motionEventDown.getY()： " + motionEventDown.getY());
        if (pd != 0) {
            if (motionEventDown.getX() < (pd / 3)) {
                if (Math.abs(motionEventDown.getX() - motionEventUp.getX()) < offDistance) {
                    if ((motionEventUp.getY() - motionEventDown.getY() - shifting) > distance) {
//                        Log.d(TAG, "左下");
                        if (mVideoGestureListener != null) {
                            mVideoGestureListener.onLBFling();
                            shifting = shifting + distance;
                        }
                    } else if ((motionEventUp.getY() - motionEventDown.getY() - shifting) < -distance) {
//                        Log.d(TAG, "左上");
                        if (mVideoGestureListener != null) {
                            mVideoGestureListener.onLUFling();
                            shifting = shifting - distance;
                        }
                    }
                }
            } else if (motionEventDown.getX() > (2 * pd / 3)) {
                if (Math.abs(motionEventDown.getX() - motionEventUp.getX()) < offDistance) {
                    if ((motionEventUp.getY() - motionEventDown.getY() - shifting) > distance) {
//                        Log.d(TAG, "右下");
                        if (mVideoGestureListener != null) {
                            mVideoGestureListener.onRBFling();
                            shifting = shifting + distance;
                        }
                    } else if ((motionEventUp.getY() - motionEventDown.getY() - shifting) < -distance) {
//                        Log.d(TAG, "右上");
                        if (mVideoGestureListener != null) {
                            mVideoGestureListener.onRUFling();
                            shifting = shifting - distance;
                        }
                    }
                }
            }
        }
        return true;
    }

    // 轻按着不松手
    @Override
    public void onShowPress(MotionEvent arg0) {
//        Log.i(TAG, "onShowPress");
    }

    // 按着又起来(action_down+action_up)
    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
//        Log.i(TAG, "onSingleTapUp");
        if (mVideoGestureListener != null) {
            mVideoGestureListener.onSingleTapUp();
        }
        return true;
    }

    public interface VideoGestureListener {
        // 向左滑,快退
        void onLeftFling();

        // 向右滑,快进
        void onRightFling();

        // 左边上滑,调高亮度
        void onLUFling();

        // 左边下滑，调低亮度
        void onLBFling();

        // 右边上滑,调高音量
        void onRUFling();

        // 右边下滑,调低音量
        void onRBFling();

        // 点击后直接放
        void onSingleTapUp();
    }

    public GestureDetector getmGestureDetector() {
        return mGestureDetector;
    }

    public VideoGestureListener getmVideoGestureListener() {
        return mVideoGestureListener;
    }

    public void setmVideoGestureListener(VideoGestureListener mVideoGestureListener) {
        this.mVideoGestureListener = mVideoGestureListener;
    }

    // 判断横竖屏
    private void ifHorizontal() {
        if (context != null) {
            Configuration mConfiguration = context.getResources().getConfiguration();
            int ori = mConfiguration.orientation; // 获取屏幕方向
            ifHorizontal = ori == Configuration.ORIENTATION_LANDSCAPE;
        }
        if (ifHorizontal) {
            pd = DeviceUtil.heightPixels;
        } else {
            pd = DeviceUtil.widthPixels;
        }
    }

}
