package com.zp.mobile91q.videoplayeractivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eostek.media.EosPlayer;
import com.eostek.media.IMediaPlayer;
import com.eostek.media.IMediaPlayer.OnBufferingUpdateListener;
import com.eostek.media.IMediaPlayer.OnCompletionListener;
import com.eostek.media.IMediaPlayer.OnErrorListener;
import com.eostek.media.IMediaPlayer.OnInfoListener;
import com.eostek.media.IMediaPlayer.OnPreparedListener;
import com.zp.mobile91q.App;
import com.zp.mobile91q.R;
import com.zp.mobile91q.pgmdetail.PgrpInfo;
import com.zp.mobile91q.pgmdetail.SourceInfo;
import com.zp.mobile91q.pgmdetail.UIHelper;
import com.zp.mobile91q.videoplayer.PolymerizePlayUrl;
import com.zp.mobile91q.videoplayer.StringUtils;
import com.zp.mobile91q.videoplayer.VideoPlayerView;
import com.zp.mobile91q.videoplayer.VideoPlayerView.VideoGestureListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoPlayerActivity extends AppCompatActivity implements OnPreparedListener, OnCompletionListener, OnErrorListener,
        OnInfoListener, OnBufferingUpdateListener, OnSeekBarChangeListener, View.OnTouchListener {
    private PgrpInfo pgrpInfo = new PgrpInfo();
    private final String TAG = "VideoPlayerActivity";
    private String sourceUrl = "";
    private String realUrl = "";
    private int serialId = 0;// 具体哪一集
    // 实际的m3u8播放地址
    protected String playUrlStr;
    private VideoPlayerView surfaceView;
    private LinearLayout play_complation;
    private LinearLayout brightnessLinearLayout,voiceLinearLayout;
    private LinearLayout video_details_ctrl_bottom;
    private SeekBar mSeekBar;
    private TextView current_time;
    private TextView total_time;
    private ImageButton play_bt;
    private static final int ONERROR = 14;
    private static final int GETCHANGESOURCEURL = 15;
    protected static final int UPDATE_PGM_DETAIL = 0;
    protected static final int PLAY_URL_READY = 1;
    protected static final int START_PLAY = 2;
    protected static final int UPDATE_SEEK_BAR = 3;
    protected static final int HIDDEN_CONTROL_BAR = 4;
    protected static final int UPDATE_COMMENT = 5;
    protected static final int UPDATE_COLLECT_STATE = 6;
    protected static final int UPDATE_PRAISE_NUM = 7;
    protected static final int UPDATE_COLLECT_DATA = 8;// 修改服务器收藏标识
    private static final int SETARTICULATIONDATA = 16;// 手动切源的数据
    private static final int ONSINGLETAPUP = 17;// 单击屏幕显示隐藏控件的msg值
    private String pgmContentId = "";//用于记录历史
    private ProgressBar brightnessProgressBar,voiceProgressBar;
    private TextView mVideoName;
    private int seekPosition;
    int tvSerialIndex;//record the index of serial choose
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置让应用主题内容占据状态栏和导航栏
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
            decorView.setSystemUiVisibility(option);
            //设置状态栏和导航栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_player);

        // 获取pgrpInfo
        pgrpInfo = App.getInstance().getPgrpInfo();
        String contentId = UIHelper.contentId;
        String channel = UIHelper.channel;
        Intent intent = getIntent();
        if (intent != null) {
            sourceUrl = intent.getStringExtra("sourceUrl");
            realUrl = intent.getStringExtra("realUrl");
            int sourcesID = intent.getIntExtra("sourcesID", 0);
            serialId = intent.getIntExtra("serialId", 0);
            seekPosition = intent.getIntExtra("seekPosition",-1);
            tvSerialIndex = intent.getIntExtra("tvSerialIndex", 0);
        }

        surfaceView = findViewById(R.id.video_details_view);
        loading = findViewById(R.id.palyer_loading); // 加载缓存UI
        tv_play_rate = findViewById(R.id.play_load_rate);
        tv_play_percent = findViewById(R.id.play_load_percent);
        play_complation = findViewById(R.id.play_complation);
        playText = findViewById(R.id.play_complation_text);
        mSeekBar = findViewById(R.id.video_progress);
        current_time = findViewById(R.id.video_current_time);
        total_time = findViewById(R.id.video_duration);
        play_bt = findViewById(R.id.video_details_play);
        play_stop_btn = findViewById(R.id.play_stop_btn);
        mSeekBar.setOnSeekBarChangeListener(this);
        voiceProgressBar = findViewById(R.id.voice_control_progress_bar);
        brightnessProgressBar = findViewById(R.id.brightness_control_progress_bar);
        brightnessLinearLayout = findViewById(R.id.brightness_control_linearlayout);
        brightnessLinearLayout = findViewById(R.id.brightness_control_linearlayout);
        voiceLinearLayout = findViewById(R.id.voice_control_linearlayout);
        video_details_ctrl_bottom = findViewById(R.id.video_details_ctrl_bottom);
        mVideoName = findViewById(R.id.video_details_title);

        // 添加手势监听
        surfaceView.setmVideoGestureListener(mVideoGestureListener);

        surfaceView.setOnTouchListener(this);// 播放界面
        // 实例化亮度音量调节工具类
        mPlaySslippingUtil = new PlaySslippingUtil(this);

        //设置亮度和音量刻度值
        Log.e(TAG,"current sys bright:"+mPlaySslippingUtil.getSysBright());
        brightnessProgressBar.setProgress(mPlaySslippingUtil.getSysBright()*100/255);
        voiceProgressBar.setProgress(mPlaySslippingUtil.getSysVoice());

        //节目名
        initPgmName();
    }

    private void initPgmName() {
        if(pgrpInfo!=null && pgrpInfo.getName().length()>0) {
            if(tvSerialIndex>0) {//剧集
                mVideoName.setText(pgrpInfo.getPgmList()[tvSerialIndex].getName());
            }else{//电影
                mVideoName.setText(pgrpInfo.getName());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pgrpInfo != null && pgrpInfo.getPgmList() != null && (pgrpInfo.getPgmList()).length > 0
                && serialId < (pgrpInfo.getPgmList()).length) {
            // 准备剧集列表信息
//            if (ifHaveEpisode) {
//                getFSEpisodeData(pgrpInfo.getPgmList());
//            }
            // 填充剧集下拉框
//            if (mHolder != null) {
//                mHolder.setFSEpisodeData(FSEpisodeData, serialId);
//            }
            SourceInfoList = (pgrpInfo.getPgmList())[serialId].getWebsiteInfoList();
            //
//            todo

            //过滤数组，只留站点一种清晰度
            ArrayList<SourceInfo> newSInfoList = new ArrayList<SourceInfo>();
            for(int i=0;i< SourceInfoList.size();i++){
                if(i==0){
                    newSInfoList.add(SourceInfoList.get(0));
                } else {
                    if(!SourceInfoList.get(0).getWebsiteCode().equals(SourceInfoList.get(i).getWebsiteCode())){
                        if(SourceInfoList.get(i).getHdName().contains("超清")){
                            newSInfoList.add(SourceInfoList.get(i));
                        }
                    }
                }
            }
            SourceInfoList.clear();
            SourceInfoList.addAll(newSInfoList);
            pgmContentId = (pgrpInfo.getPgmList())[serialId].getPgmContentId();
//            getArticulationData(SourceInfoList);
        }
        if(!TextUtils.isEmpty(realUrl)){
            playUrlStr = realUrl;
            play();
            return;
        }
        if (TextUtils.isEmpty(playUrlStr)) {
            getPlayM3U8(sourceUrl);
        } else {
            play();
        }
    }

    private int enterTime = 0;// 当前时间
    private String wc = "";// 站点
    private boolean ifcracking = false;// 是否是破解出错
    private boolean ifbuffering = false;// 是否是缓冲出错
    private boolean ifCanSeekTo = false;
    private int changeSourceUrlIndex = 0;// 循环切源
    // 包含站点的清晰度的播放地址
    private ArrayList<SourceInfo> SourceInfoList = new ArrayList<SourceInfo>();
    // 没有用过的源信息
    private ArrayList<SourceInfo> sourceUrlIndexList = new ArrayList<SourceInfo>();
    // 跟随变化的数据源索引
    private int handChangeSourceIndex = 0;
    // 当前播放地址
    private String nowSourceUrl = "";
    private boolean ifOnStop = false;//播放地址获取和启动播放是异步操作，Activity 启动 onDestory 方法之后，也有可能被启动
    /**
     * 通常的播放流程，没有历史记录时，更换剧集时进入此流程
     */
    private void getPlayM3U8(final String sourceUrl) {
        // 获取当前时间
        enterTime = (int) android.os.SystemClock.elapsedRealtime();
        ifcracking = false;
        ifbuffering = false;
        ifCanSeekTo = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int hd = 1;
                changeSourceUrlIndex = 0;
                if (SourceInfoList != null && SourceInfoList.size() > 0 && sourceUrlIndexList != null) {
                    sourceUrlIndexList.clear();
                    sourceUrlIndexList.addAll(SourceInfoList);
                    for (int i = 0; i < SourceInfoList.size(); i++) {
                        if (sourceUrl != null && sourceUrl.equals(SourceInfoList.get(i).getPlayUrl())) {
                            changeSourceUrlIndex = i;
                        }
                    }
                }
                handChangeSourceIndex = changeSourceUrlIndex;
                if (sourceUrlIndexList != null && changeSourceUrlIndex < sourceUrlIndexList.size()) {
                    sourceUrlIndexList.remove(changeSourceUrlIndex);
                }// 删掉用过的源信息
                if (SourceInfoList != null && SourceInfoList.size() > changeSourceUrlIndex) {
                    Log.e(TAG, "playurl：" + SourceInfoList.get(changeSourceUrlIndex).getPlayUrl());
                    nowSourceUrl = SourceInfoList.get(changeSourceUrlIndex).getPlayUrl();
                    wc = SourceInfoList.get(changeSourceUrlIndex).getWebsiteCode();
                    // 地址为空，破解出错
                    if (StringUtils.isEmpty(SourceInfoList.get(changeSourceUrlIndex).getPlayUrl())) {
                        // 记录错误
                        ifcracking = true;
                    }
                    getPlayUrl(SourceInfoList.get(changeSourceUrlIndex).getPlayUrl(), hd);
                }
                if (!ifOnStop) {
                    updateHandler.sendEmptyMessage(PLAY_URL_READY);
//                    updateHandler.sendEmptyMessage(SETARTICULATIONDATA);
                }
            }
        }).start();
    }

    private PolymerizePlayUrl playUrl = new PolymerizePlayUrl();
    private String userAgent = "";
    private void getPlayUrl(String url, int hd) {
        tv_play_percent.setText("获取播放地址中...");
        if (playUrl != null) {
            String url2 = playUrl.getDemand(url, hd);
            if (!StringUtils.isEmpty(url2)) {
                String[] list = url2.split("sciflyku");
                if (list.length > 1) {
                    playUrlStr = list[0];
                    userAgent = list[1];
                } else if (list.length == 1) {
                    playUrlStr = list[0];
                    userAgent = "";
                } else {
                    playUrlStr = "";
                    userAgent = "";
                }
                runOnUIThread(tv_play_percent,"获取地址成功");
            } else {
                //获取地址为空时，走onError
//                onError(0,0);
//            	getPlayM3U8Again();
//            	mHandler.sendEmptyMessage(MEDIA_COMPLETION);
//            	Log.e("ZZP", "playUrlStr： " + playUrlStr);
                // 弹出Toast提示
                updateHandler.removeMessages(GETCHANGESOURCEURL);
                updateHandler.sendEmptyMessageDelayed(GETCHANGESOURCEURL, 500);
            }
        } else {
            playUrlStr = "";
            userAgent = "";
        }
    }

    private int pt = -1;// 破解时间
    private EosPlayer mPlayer = null;
    SurfaceHolder sHolder;
    // 网络断开后停止播放
    private boolean ifNetBreakStopMPlayer = false;
    private void play() {
        Log.i(TAG, "currentPlayPath: " + playUrlStr);
        if (!ifOnStop) {
            ifbuffering = true;
            if (enterTime != 0) {
                // 获取破解时间
                pt = (int) ((android.os.SystemClock.elapsedRealtime() - enterTime) / 1000);
                // 获取当前时间 给缓冲时间做准备
                enterTime = (int) android.os.SystemClock.elapsedRealtime();
            }
            if (playUrlStr == null) {
                return;
            }
            try {
                if (mPlayer != null) {
                    mPlayer.release();
                }
                mPlayer = new EosPlayer();
                sHolder = surfaceView.getHolder();
                sHolder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {
                        mPlayer.setDisplay(surfaceHolder);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                    }
                });
                mPlayer.setParameter(IMediaPlayer.INVOKE_ID_SET_OVERLAY_FORMAT, IMediaPlayer.SDL_FCC_RV32);
                // 设置当前播放视频信息.
                Map<String, String> map = new HashMap<String, String>();
                if (!StringUtils.isEmpty(userAgent)) {
                    map.put("User-Agent", userAgent);
                } else {
                    map.put("User-Agent", "AppleCoreMedia/1.0.0.11B511 (iPad; U; CPU OS 7_0_3 like Mac OS X; zh_cn)");
                }
                Log.e(TAG, "m3u8: " + playUrlStr);
                //在此判断使用软解或者硬解
                if(playUrlStr!=null && playUrlStr.contains("@@@decode=2")) {
                    playUrlStr = playUrlStr.split("@@@")[0];//取出前面的url，后面的一段去掉不要
                }
                mPlayer.setDataSource(playUrlStr, map);
                mPlayer.setOnBufferingUpdateListener(this);
                mPlayer.setOnErrorListener(this);
                mPlayer.setOnInfoListener(this);
                mPlayer.setOnPreparedListener(this);
                mPlayer.setOnCompletionListener(this);
                // 添加手势监听
//                mHolder.getPlayView().setmVideoGestureListener(mVideoGestureListener);
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
        Log.i(TAG, "onError(): " + "ifcracking: " + ifcracking + "ifbuffering: " + ifbuffering);
        // 切源 开始
        Log.e(TAG, "changeSourceUrlIndex=" + changeSourceUrlIndex);
        if (sourceUrlIndexList != null && sourceUrlIndexList.size() > 0) {
            Log.e(TAG, "sourceUrlIndexList.size()=" + sourceUrlIndexList.size());
            ifError = true;
            if (sourceUrlIndexList.size() <= changeSourceUrlIndex) {
                changeSourceUrlIndex = 0;
            }
            // 弹出Toast提示
            updateHandler.removeMessages(GETCHANGESOURCEURL);
            updateHandler.sendEmptyMessageDelayed(GETCHANGESOURCEURL, 500);//3000
        } else {
            // 切光退出
            Log.e(TAG, "no url");
            updateHandler.sendEmptyMessage(ONERROR);
        }
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        onStatus(iMediaPlayer, i, i1, -1);
        return false;
    }

    public void onStatus(IMediaPlayer mp, int arg1, int arg2, int type) {
        if (type == 0) {
            Message msg = mHandler.obtainMessage();
            msg.what = MEDIA_BUFFER_UPDSATE; // 进度条
            msg.arg1 = arg1; // 进度百分比
            mHandler.sendMessage(msg);
            return;
        } else if (type == 1) {
            mHandler.sendEmptyMessage(MEDIA_COMPLETION);
            return;
        }
        if (arg1 == IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH) {
            Message msg = mHandler.obtainMessage();
            msg.what = IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH; // 下载速度
            msg.arg1 = arg2; // 速度单位是Bps
            mHandler.sendMessage(msg);
        } else if(arg1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            Message msg = mHandler.obtainMessage();
            msg.what = IMediaPlayer.MEDIA_INFO_BUFFERING_END; // 下载速度
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(arg1);
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (!ifOnStop) {
            ifCanSeekTo = true;
            duration = (int) mPlayer.getDuration();
            if(seekPosition>-1)
                mPlayer.seekTo(seekPosition);//设置播放进度
            mPlayer.start();
            ifbuffering = false;
            if (enterTime != 0) {
                // 获取缓冲时间
//                bt = (int) ((android.os.SystemClock.elapsedRealtime() - enterTime) / 1000);
            }
            ifNetBreakStopMPlayer = false;// 修改标示
            Log.i(TAG, "duration : " + duration);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSeekBar.setMax(duration);
                    total_time.setText(Tools.formatDurationHMS(duration / 1000));
                    play_bt.setImageResource(R.drawable.btn_control_pause);
                    play_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            responsePlayButton();
                        }
                    });
                }
            });
            updateHandler.sendEmptyMessage(UPDATE_SEEK_BAR);
        }
    }

    private LinearLayout loading; // 缓冲板
    private TextView tv_play_rate, tv_play_percent, playText; // 缓冲的速度，和百分比
    private final static int MEDIA_BUFFER_UPDSATE = 0x00001111; // 播放器更新进度百分比
    private final static int MEDIA_COMPLETION = 0x00011111; // 播放器播放完成
    // 是否在缓冲，默认没有缓冲
    private boolean ifcache = false;
    // 是否出错
    private boolean ifError = false;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START: // 缓存开始
                    Log.e(TAG,"缓存开始");
                    loading.setVisibility(View.VISIBLE);
                    ifcache = true;// 正在缓冲
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END: // 缓存结束
                    Log.e(TAG,"缓存结束");
                    loading.setVisibility(View.GONE);
                    ifcache = false;// 缓冲结束
                    //重设画面比例为等比
                    Log.e(TAG,"重设画面比例为等比");
                    VideoSizeTool.setVideoRatio(true, mPlayer.getVideoWidth(),mPlayer.getVideoHeight(),surfaceView);
                    break;
                case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH: // 加载速度
                    int rate = msg.arg1 / 1024;
                    if (rate > 0)
                        tv_play_rate.setText(rate + "k/s");
                    break;
                case MEDIA_BUFFER_UPDSATE: // 加载进度百分比
                    int percent = msg.arg1;
                    if (percent > 0 && percent < 100) {
                        String fg = getString(R.string.play_load_percent);
                        tv_play_percent.setText(fg + percent + "%");
                        Log.e(TAG,"加载进度百分比" + percent + "%");
                    }
                    break;
                case MEDIA_COMPLETION:
                    Log.e(TAG, "ifError:" + ifError);
                    // 先关掉播放器
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.release();
                        updateHandler.removeMessages(UPDATE_SEEK_BAR);
                        mPlayer = null;
                    }
                    if (ifError) {
                        playText.setText(getString(R.string.play_url_change));
                    } else {
                        if (pgrpInfo != null && !"movie".equals(pgrpInfo.getChannel_code())
                                && serialId + 1 < pgrpInfo.getPgmList().length && serialId + 1 > 0) {
                            Log.e(TAG, "play next");// 播放下一集
                            playText.setText(getString(R.string.player_change_next_one));
                            // 填充剧集下拉框 ,修改当前集数
//                            if (mHolder != null) {
//                                mHolder.setFSEpisodeData(FSEpisodeData, serialId + 1);
//                            }
                            // 自动切换到下一集 播放
//                            clickFSEpisode(serialId + 1);
                        } else {
                            // 播放完成之后的提示
//                            if (pgrpInfo != null && "movie".equals(pgrpInfo.getChannel_code()) && mHolder != null
//                                    && mHolder.getVideoPlayOverLinearLayout() != null) {
//                                playText.setVisibility(View.GONE);
//                                mHolder.getVideoPlayOverLinearLayout().setVisibility(View.VISIBLE);
//                                loading.setVisibility(View.GONE);
//                            } else {
//                                playText.setVisibility(View.VISIBLE);
//                                playText.setText(getString(R.string.play_complation));
//                            }
                        }
                    }
                    if (!play_complation.isShown()) {
                        play_complation.setVisibility(View.VISIBLE);
                    }
                    break;
                case ONSINGLETAPUP:
                    isShowCtrbar(false);
                    break;
            }
        }
    };

    private int currentPosition = 0;
    private int duration = 0;
    private static final int BRIGHTNESS_CONTROL_NOT_SHOW = 11;
    private static final int VOICE_CONTROL_NOT_SHOW = 12;
    private static final int UPDATE_SEEKBAR = 13;
    private int result = 0;
    // 快进初始值
    private int initPosition = 0;
    // 最后需要快进或快退的值
    private int resultOffset = 0;
    @SuppressLint("HandlerLeak")
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message mes) {
            int what = mes.what;
            switch (what) {
                case UPDATE_PGM_DETAIL:
//                    refreshView();
                    break;
                case PLAY_URL_READY:
                    ifError = false;
                    if (play_complation.isShown()) {
                        play_complation.setVisibility(View.GONE);
                    }
                    if(TextUtils.isEmpty(playUrlStr)){
                        Log.e("ZZP","plauUrlStr is null");
                    }
                    play();
                    break;
                case UPDATE_SEEK_BAR:
                    // 更新进度条
                    if (mPlayer != null) {
                        currentPosition = (int) mPlayer.getCurrentPosition();
                        mSeekBar.setProgress(currentPosition);
                        current_time.setText(Tools.formatDurationHMS(currentPosition / 1000));
                        updateHandler.sendEmptyMessageDelayed(UPDATE_SEEK_BAR, 500);
                        if (duration <= currentPosition) {
                            duration = (int) mPlayer.getDuration();
                            mSeekBar.setMax(duration);
                            total_time.setText(Tools.formatDurationHMS(duration / 1000));
                        }
                    }
                    break;
                case UPDATE_COLLECT_DATA:
//                    if (collectEPG(isCollected)) {
//                        if (pgrpInfo.getName() != null) {
//                            Toast.makeText(getBaseContext(), isCollected ? "《" +
//                                    pgrpInfo.getName() + "》   " + getString(R.string.video_Favorites)
//                                    + getString(R.string.video_collect_success) : "《" + pgrpInfo.getName() + "》   "
//                                    + getString(R.string.video_collect_cancle)
//                                    + getString(R.string.video_collect_success), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    } else {
//                        if (pgrpInfo.getName() != null) {
//                            // 失败时返回上一次
//                            isCollected = !isCollected;
//                            updateCollectState();
//                            Toast.makeText(getBaseContext(), isCollected ? "《" +
//                                    pgrpInfo.getName() + "》   " + getString(R.string.video_Favorites)
//                                    + getString(R.string.video_collect_fail) : "《" + pgrpInfo.getName()
//                                    + "》   " + getString(R.string.video_collect_cancle)
//                                    + getString(R.string.video_collect_fail), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    }
                    break;
                case BRIGHTNESS_CONTROL_NOT_SHOW:
                    brightnessLinearLayout.setVisibility(View.GONE);
                    break;
                case VOICE_CONTROL_NOT_SHOW:
                    voiceLinearLayout.setVisibility(View.GONE);
                    break;
                case UPDATE_SEEKBAR:
                    if (mPlayer != null) {
                        mPlayer.seekTo(result);
                        initPosition = 0;
                        result = 0;
                        resultOffset = 0;
                        // 进度条刷新
//                        mHolder.getMoveText().setVisibility(View.GONE);
                        updateHandler.sendEmptyMessage(UPDATE_SEEK_BAR);
                    }
                    break;
                case ONERROR:
                    // 出错或者切光 退出
                    if (mPlayer != null) {
                        mPlayer.stop();
                        Toast.makeText(VideoPlayerActivity.this, R.string.play_err, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case GETCHANGESOURCEURL:
                	playText.setText(getString(R.string.play_url_change));
//                    getPlayM3U8Again();// 切源播放
                    break;
//                case CHANGEOK:
//                    if (mHolder != null && mHolder.getArticulation() != null) {
//                        mHolder.getArticulation().setEnabled(true);
//                    }
//                    break;
            }
        }
    };

    public void responsePlayButton() {
        if (!ifNetBreakStopMPlayer) {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    localPause();
                } else {
                    localResume();
                }
            }
        }
    }
    private ImageView play_stop_btn;
    private void localPause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            loading.setVisibility(View.GONE);
            play_bt.setImageResource(R.drawable.btn_control_play);
            play_stop_btn.setVisibility(View.VISIBLE);
            updateHandler.removeMessages(UPDATE_SEEK_BAR);
        }
    }
    private void localResume() {
        if (mPlayer != null) {
            mPlayer.start();
            if (ifcache) {
                loading.setVisibility(View.VISIBLE);
            } else {
                loading.setVisibility(View.GONE);
            }
            play_bt.setImageResource(R.drawable.btn_control_pause);
            play_stop_btn.setVisibility(View.GONE);
            updateHandler.sendEmptyMessage(UPDATE_SEEK_BAR);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    void runOnUIThread(final TextView v, final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                v.setText(str);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int position = seekBar.getProgress();
        setPlayPosition(position);
    }

    public void setPlayPosition(int position) {
        if (mPlayer != null && ifCanSeekTo) {
            position = (duration < position) ? duration : position;
            mPlayer.seekTo(position);
        }
    }

    /**
     * 亮度调节和音量调节工具类
     */
    private PlaySslippingUtil mPlaySslippingUtil;
    /**
     * 播放器手势操作
     */
    private VideoGestureListener mVideoGestureListener = new VideoGestureListener() {

        // 向左滑,快退
        @Override
        public void onLeftFling() {
            Log.e(TAG, "FR   " + current_time.getText());
            if (mPlayer != null && mPlayer.isPlaying() && !"--:--".equals(current_time.getText())) {
                // 第一次滑的时候
                if (initPosition == 0) {
                    initPosition = (int) mPlayer.getCurrentPosition();
                    resultOffset = 0;
                }
//                forwardRewind(false);
            }
        }

        // 向右滑,快进
        @Override
        public void onRightFling() {
            Log.e(TAG, "FF " + current_time.getText());
            if (mPlayer != null && mPlayer.isPlaying() && !"--:--".equals(current_time.getText())) {
                // 第一次滑的时候
                if (initPosition == 0) {
                    initPosition = (int) mPlayer.getCurrentPosition();
                    resultOffset = 0;
                }
//                forwardRewind(true);
            }
        }

        // 右边上滑,调高音量
        @Override
        public void onRUFling() {
            setVolume(true);
        }

        // 右边下滑,调低音量
        @Override
        public void onRBFling() {
            setVolume(false);
        }

        // 左边上滑,调高亮度
        @Override
        public void onLUFling() {
            // 手动调整亮度
            if (mPlaySslippingUtil != null) {
                float brightness = mPlaySslippingUtil.getCurrentBrightness();
                if(brightness==-1f) {//-1代表没有值，这时候取系统值
                    brightness = mPlaySslippingUtil.getSysBright() / 255f;
                }
                if(brightness<1.0f) brightness += 0.05f;
                Log.e("ZZP","brightness:"+brightness);
//                setScreenBrightness(brightness);
                mPlaySslippingUtil.saveScreenBrightness(brightness);
                // 亮度进度条显示
                int percent = (int) (brightness * 100);
                Log.e("ZZP","percent:"+percent);
                brightnessProgressBar.setProgress(percent);
                brightnessLinearLayout.setVisibility(View.VISIBLE);
                updateHandler.removeMessages(BRIGHTNESS_CONTROL_NOT_SHOW);
                updateHandler.sendEmptyMessageDelayed(BRIGHTNESS_CONTROL_NOT_SHOW, 1000);
            }
        }

        // 左边下滑，调低亮度
        @Override
        public void onLBFling() {
            if (mPlaySslippingUtil != null) {
                float brightness = mPlaySslippingUtil.getCurrentBrightness();
                if(brightness==-1f) {//-1代表没有值，这时候取系统值
                    brightness = mPlaySslippingUtil.getSysBright() / 255f;
                }
                if(brightness>0.05f) brightness -= 0.05f;
                Log.e("ZZP","brightness:"+brightness);
//                setScreenBrightness(brightness);
                mPlaySslippingUtil.saveScreenBrightness(brightness);
                // 亮度进度条显示
                int percent = (int) (brightness * 100);
                Log.e("ZZP","percent:"+percent);
                brightnessProgressBar.setProgress(percent);
                brightnessLinearLayout.setVisibility(View.VISIBLE);
                updateHandler.removeMessages(BRIGHTNESS_CONTROL_NOT_SHOW);
                updateHandler.sendEmptyMessageDelayed(BRIGHTNESS_CONTROL_NOT_SHOW, 1000);
            }
        }

        @Override
        public void onSingleTapUp() {
            if (video_details_ctrl_bottom.isShown()) {
                isShowCtrbar(false);
                mHandler.removeMessages(ONSINGLETAPUP);
            } else {
                isShowCtrbar(true);
                mHandler.sendEmptyMessageDelayed(ONSINGLETAPUP, 8000);
            }
        }
    };

    public void setScreenBrightness(float paramInt) {
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = paramInt / 255.0F;
        localWindow.setAttributes(localLayoutParams);
    }

    private void setVolume(boolean isUp) {
        if (mPlaySslippingUtil != null && mPlaySslippingUtil.getmAudioManager() != null) {
            int voiceUpdate = 0;
            int voiceNow = mPlaySslippingUtil.getmAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC);
            if (isUp) {
                voiceUpdate = voiceNow + 1;
                if (voiceUpdate > mPlaySslippingUtil.getMaxVoice()) {
                    mPlaySslippingUtil.getmAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC,
                            mPlaySslippingUtil.getMaxVoice(), 0);
                    voiceProgressBar.setProgress(100);

                } else {
                    mPlaySslippingUtil.getmAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC, voiceUpdate, 0);
                    voiceProgressBar.setProgress(voiceUpdate * 100 / mPlaySslippingUtil.getMaxVoice());
                }
            } else {
                voiceUpdate = voiceNow - 1;
                if (voiceUpdate < 0) {
                    mPlaySslippingUtil.getmAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    voiceProgressBar.setProgress(0);
                } else {
                    mPlaySslippingUtil.getmAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC, voiceUpdate, 0);
                    voiceProgressBar.setProgress(voiceUpdate * 100 / mPlaySslippingUtil.getMaxVoice());
                }
            }
            voiceLinearLayout.setVisibility(View.VISIBLE);
            updateHandler.removeMessages(VOICE_CONTROL_NOT_SHOW);
            updateHandler.sendEmptyMessageDelayed(VOICE_CONTROL_NOT_SHOW, 1000);
        }
    }

    public void isShowCtrbar(boolean state) {
        if (state) {
            video_details_ctrl_bottom.setVisibility(View.VISIBLE);
            mVideoName.setVisibility(View.VISIBLE);
        } else {
            video_details_ctrl_bottom.setVisibility(View.GONE);
            mVideoName.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return surfaceView.getmGestureDetector().onTouchEvent(motionEvent);
    }
}
