package com.zp.mobile91q.pgmdetail;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eostek.media.EosPlayer;
import com.eostek.media.IMediaPlayer;
import com.zp.mobile91q.App;
import com.zp.mobile91q.R;
import com.zp.mobile91q.videoplayeractivity.VideoPlayerActivity;
import com.zp.mobile91q.videoplayeractivity.VideoSizeTool;
import com.zp.mobile91q.videoplayer.StringUtils;
import com.zp.mobile91q.videoplayer.VideoPlayerView;
import com.zp.mobile91q.videoplayer.VideoPlayerView.VideoGestureListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInitAndPlayTool implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, View.OnTouchListener {
    private boolean ifOnStop = false;//播放地址获取和启动播放是异步操作，Activity 启动 onDestory 方法之后，也有可能被启动
    private int enterTime = 0;// 当前时间
    private String wc = "";// 站点
    private boolean ifcracking = false;// 是否是破解出错
    private boolean ifbuffering = false;// 是否是缓冲出错
    private boolean ifCanSeekTo = false;
    private int changeSourceUrlIndex = 0;// 循环切源
    private int pt = -1;// 破解时间
    private EosPlayer mPlayer = null;
    SurfaceHolder sHolder;
    private Context context;
    public List<String> spareUrls = new ArrayList<>();
    public List<String> agents = new ArrayList<>();
    private VideoPlayerView mPlayView;
    private RelativeLayout mRl;
    private ImageButton video_details_fullscreen;
    private RelativeLayout video_details_bottom_rl;
    private static final int ONSINGLETAPUP = 17;// 单击屏幕显示隐藏控件的msg值
    private String currentUrl;
    private long duration;
    public int tvSerialIndex;

    public void play(String playUrlStr, String userAgent, VideoPlayerView playView, RelativeLayout rl) {
        //在此判断使用软解或者硬解
        if(playUrlStr!=null && playUrlStr.contains("@@@decode=2")) {
            playUrlStr = playUrlStr.split("@@@")[0];//取出前面的url，后面的一段去掉不要
        }
        currentUrl = playUrlStr;
        if(playView!=null) {
            context = playView.getContext();
            mPlayView = playView;
            mRl = rl;
            initViews();
        }
        if (!ifOnStop) {
            ifbuffering = true;
            if (enterTime != 0) {
                // 获取破解时间
                pt = (int) ((android.os.SystemClock.elapsedRealtime() - enterTime) / 1000);
                // 获取当前时间 给缓冲时间做准备
                enterTime = (int) android.os.SystemClock.elapsedRealtime();
            }
            try {
                if (mPlayer != null) {
                    mPlayer.release();
                }
                mPlayer = new EosPlayer();
                sHolder = playView.getHolder();
                mPlayer.setDisplay(sHolder);
                mPlayer.setParameter(IMediaPlayer.INVOKE_ID_SET_OVERLAY_FORMAT, IMediaPlayer.SDL_FCC_RV32);
                // 设置当前播放视频信息.
                Map<String, String> map = new HashMap<String, String>();
                if (!StringUtils.isEmpty(userAgent)) {
                    map.put("User-Agent", userAgent);
                } else {
                    map.put("User-Agent", "AppleCoreMedia/1.0.0.11B511 (iPad; U; CPU OS 7_0_3 like Mac OS X; zh_cn)");
                }
                mPlayer.setDataSource(playUrlStr, map);
                mPlayer.setOnBufferingUpdateListener(this);
                mPlayer.setOnErrorListener(this);
                mPlayer.setOnInfoListener(this);
                mPlayer.setOnPreparedListener(this);
                mPlayer.setOnCompletionListener(this);
                mPlayer.prepareAsync();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        // 添加手势监听
        mPlayView.setmVideoGestureListener(mVideoGestureListener);
        mPlayView.setOnTouchListener(this);// 播放界面
        video_details_fullscreen = mRl.findViewById(R.id.video_details_fullscreen);
        video_details_bottom_rl = mRl.findViewById(R.id.video_details_bottom_rl);
        video_details_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("realUrl", currentUrl);
                intent.putExtra("tvSerialIndex", tvSerialIndex);
//                intent.putExtra("serialId", serialId);
//                intent.putExtra("ifItHasHistory", ifPushHistory);
                if(mPlayer!=null && mPlayer.getDuration()>0){
                    intent.putExtra("seekPosition", mPlayer.getDuration());// 避免有历史记录时走到播放流程
                }
                context.startActivity(intent);
                release();
            }
        });
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int arg1, int arg2) {
        onStatus(iMediaPlayer, arg1, arg2, 0);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
//        onStatus(iMediaPlayer, 0, 0, 1);
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//        if (sourceUrlIndexList != null && sourceUrlIndexList.size() > 0) {
//            Log.e(TAG, "sourceUrlIndexList.size()=" + sourceUrlIndexList.size());
//            ifError = true;
//            if (sourceUrlIndexList.size() <= changeSourceUrlIndex) {
//                changeSourceUrlIndex = 0;
//            }
//            // 弹出Toast提示
//            updateHandler.removeMessages(GETCHANGESOURCEURL);
//            updateHandler.sendEmptyMessageDelayed(GETCHANGESOURCEURL, 500);//3000
//        } else {
//            // 切光退出
//            Log.e(TAG, "no url");
//            updateHandler.sendEmptyMessage(ONERROR);
//        }
        Log.e("PlayTool", "i: "+i+" i1: "+i1+" url: "+currentUrl);
        switch(i) {
            case MEDIA_INFO_UNKNOWN:
            case MEDIA_INFO_VIDEO_INTERRUPT:
            case MEDIA_ERROR_IO:
            case MEDIA_ERROR_MALFORMED:
            case MEDIA_ERROR_UNSUPPORTED:
            case MEDIA_ERROR_TIMED_OUT:
                toast("视频播放失败，错误码: "+i);
                //这里使用handler轮询，查看备用地址是否有值，超过15秒就退出activity
                handler.sendEmptyMessage(1001);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        onStatus(iMediaPlayer, i, i1, -1);
        return false;
    }

    public void onStatus(IMediaPlayer mp, int arg1, int arg2, int type) {
//        if (type == 0) {
//            Message msg = mHandler.obtainMessage();
//            msg.what = MEDIA_BUFFER_UPDSATE; // 进度条
//            msg.arg1 = arg1; // 进度百分比
//            mHandler.sendMessage(msg);
//            return;
//        } else if (type == 1) {
//            mHandler.sendEmptyMessage(MEDIA_COMPLETION);
//            return;
//        }
//        if (arg1 == IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH) {
//            Message msg = mHandler.obtainMessage();
//            msg.what = IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH; // 下载速度
//            msg.arg1 = arg2; // 速度单位是Bps
//            mHandler.sendMessage(msg);
//        } else if(arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
//            Message msg = mHandler.obtainMessage();
//            msg.what = IMediaPlayer.MEDIA_INFO_BUFFERING_END; // 下载速度
//            mHandler.sendMessage(msg);
//        } else {
//            mHandler.sendEmptyMessage(arg1);
//        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (!ifOnStop) {
            ifCanSeekTo = true;
//            duration = (int) mPlayer.getDuration();
            mPlayer.start();
            ifbuffering = false;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mSeekBar.setMax(duration);
//                    total_time.setText(Tools.formatDurationHMS(duration / 1000));
//                    play_bt.setImageResource(R.drawable.btn_control_pause);
//                    play_bt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {
//                            responsePlayButton();
//                        }
//                    });
//                }
//            });
//            updateHandler.sendEmptyMessage(UPDATE_SEEK_BAR);
            VideoSizeTool.setVideoRatio(mPlayer.getVideoWidth(),mPlayer.getVideoHeight(),mRl);
            copyToClipboard(context,currentUrl);
        }
    }

    private int totalDuration;//等待次数5次，就是15秒就退出此页面
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1001){
                boolean hasUrl = playSpare();
                if(hasUrl) {
                    Log.e("handler","hasUrl");
                } else {
                    Log.e("handler","spare url is't exit");
                    if(totalDuration>3){
                        totalDuration=0;
                        //告诉activity退出
                        Log.e("handler","告诉activity退出");
                        if(mListener!=null){
                            mListener.finish();
                        }
                    }else {
                        handler.sendEmptyMessageDelayed(1001,3000);
                        totalDuration++;
                    }
                }
            }
        }
    };

    private int curentSpareIndex = 0;
    private boolean playSpare() {
        if(spareUrls.size()>curentSpareIndex+1 && agents.size()>curentSpareIndex+1) {
            play(spareUrls.get(curentSpareIndex),agents.get(curentSpareIndex), mPlayView, mRl);
            curentSpareIndex++;
            return true;
        } else {
            curentSpareIndex=0;
            return false;
        }
    }

    public void release() {
        if (mPlayer!=null) {
            try{
                mPlayer.reset();
                mPlayer.release();
                mPlayer=null;
            } catch(IllegalStateException e){
                e.printStackTrace();
            }
        }
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mPlayView.getmGestureDetector().onTouchEvent(motionEvent);
    }

    public interface finishListener{
        void finish();
    }
    private finishListener mListener;
    public void setFinishListener(finishListener listener){
        mListener = listener;
    }

    /**
     * 播放器手势操作
     */
    private VideoGestureListener mVideoGestureListener = new VideoGestureListener() {

        // 向左滑,快退
        @Override
        public void onLeftFling() {
        }

        // 向右滑,快进
        @Override
        public void onRightFling() {
        }

        // 右边上滑,调高音量
        @Override
        public void onRUFling() {
        }

        // 右边下滑,调低音量
        @Override
        public void onRBFling() {
        }

        // 左边上滑,调高亮度
        @Override
        public void onLUFling() {
        }

        // 左边下滑，调低亮度
        @Override
        public void onLBFling() {
        }

        @Override
        public void onSingleTapUp() {
            if (video_details_bottom_rl.isShown()) {
                video_details_bottom_rl.setVisibility(View.GONE);
                mHandler.removeMessages(ONSINGLETAPUP);
            } else {
                video_details_bottom_rl.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(ONSINGLETAPUP, 8000);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ONSINGLETAPUP:
                    video_details_bottom_rl.setVisibility(View.GONE);
                    break;
            }
        }
    };
    static final int MEDIA_INFO_UNKNOWN = 1;//未知信息
    static final int MEDIA_INFO_STARTED_AS_NEXT = 2;//播放下一条
    static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频开始整备中，准备渲染
    static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;//视频日志跟踪
    static final int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲中 开始缓冲
    static final int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
    static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;//网络带宽，网速方面
    static final int MEDIA_INFO_BAD_INTERLEAVING = 800;//
    static final int MEDIA_INFO_NOT_SEEKABLE = 801;//不可设置播放位置，直播方面
    static final int MEDIA_INFO_METADATA_UPDATE = 802;//
    static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;//不支持字幕
    static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;//字幕超时
    static final int MEDIA_INFO_VIDEO_INTERRUPT= -10000;//数据连接中断，一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
    static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频方向改变，视频选择信息
    static final int MEDIA_INFO_AUDIO_RENDERING_START = 10002;//音频开始整备中
    static final int MEDIA_ERROR_SERVER_DIED = 100;//服务挂掉，视频中断，一般是视频源异常或者不支持的视频类型。
    static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
    static final int MEDIA_ERROR_IO = -1004;//IO 错误
    static final int MEDIA_ERROR_MALFORMED = -1007;
    static final int MEDIA_ERROR_UNSUPPORTED = -1010;//数据不支持
    static final int MEDIA_ERROR_TIMED_OUT = -110;//数据超时

    void toast(String str) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    /**
     * 复制到剪贴板
     * @param context  上下文
     * @param text  要复制的内容
     */
    public void copyToClipboard(Context context, String text){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        assert cm != null;
        cm.setPrimaryClip(ClipData.newPlainText("text",text));
        Log.e("ZZ","粘贴成功");
        toast("播放地址复制到粘贴板成功");
    }
}
/*ijk错误码
 * Do not change these values without updating their counterparts in native
    int MEDIA_INFO_UNKNOWN = 1;//未知信息
    int MEDIA_INFO_STARTED_AS_NEXT = 2;//播放下一条
    int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频开始整备中，准备渲染
    int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;//视频日志跟踪
    int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲中 开始缓冲
    int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
    int MEDIA_INFO_NETWORK_BANDWIDTH = 703;//网络带宽，网速方面
    int MEDIA_INFO_BAD_INTERLEAVING = 800;//
    int MEDIA_INFO_NOT_SEEKABLE = 801;//不可设置播放位置，直播方面
    int MEDIA_INFO_METADATA_UPDATE = 802;//
    int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;//不支持字幕
    int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;//字幕超时
    int MEDIA_INFO_VIDEO_INTERRUPT= -10000;//数据连接中断，一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
    int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频方向改变，视频选择信息
    int MEDIA_INFO_AUDIO_RENDERING_START = 10002;//音频开始整备中
    int MEDIA_ERROR_SERVER_DIED = 100;//服务挂掉，视频中断，一般是视频源异常或者不支持的视频类型。
    int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
    int MEDIA_ERROR_IO = -1004;//IO 错误
    int MEDIA_ERROR_MALFORMED = -1007;文件格式错误
    int MEDIA_ERROR_UNSUPPORTED = -1010;//数据不支持
    int MEDIA_ERROR_TIMED_OUT = -110;//数据超时

*/