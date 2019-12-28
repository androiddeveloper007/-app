package com.zp.mobile91q.pgmdetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.zp.mobile91q.App;
import com.zp.mobile91q.R;
import com.zp.mobile91q.pgmdetail.PgrpInfo.PgmInfo;
import com.zp.mobile91q.popup.JujiBottomPopup;
import com.zp.mobile91q.tool.BtnClickAnimTool;
import com.zp.mobile91q.videoplayer.StringUtils;
import com.zp.mobile91q.videoplayer.VideoPlayerView;

import java.util.Arrays;

public class PgmDetailActivity extends AppCompatActivity {
    private static final String TAG = "PgmDetailActivity";
    private String channel = "";
    private String contentId = "";
    private PgmInfo[] pgmList = null;
    private PgrpInfo pgrpInfo;
    private boolean isMovie = false;
    private TextView video_detail_epgName;
    private TextView video_detail_dirertorName;
    private TextView video_detail_actorName;
    private TextView description;
    private TextView video_detail_typeName;
    private TextView tv_publish_time;
    private ImageView video_detail_img;
    private ImageView video_play;
    private PlayerInitAndPlayTool tool;
    boolean getRealPlayUrlSuccess;
    private BasePopupView popupView;
    int tvSerialIndex;//record the index of serial choose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_pgm_detail);
        datas();
        views();
    }

    private void views() {
        video_detail_epgName = findViewById(R.id.video_detail_pgmName);
        video_detail_dirertorName = findViewById(R.id.video_detail_dirertorName);
        video_detail_actorName = findViewById(R.id.video_detail_actorName);
        description = findViewById(R.id.info);
        video_detail_typeName = findViewById(R.id.video_detail_typeName);
        tv_publish_time = findViewById(R.id.tv_publish_time);
        video_detail_img = findViewById(R.id.video_detail_logo_play);
        video_play = findViewById(R.id.video_play);
        video_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });
        BtnClickAnimTool.setBtnClkAnim(findViewById(R.id.video_play));
    }

    private void datas() {
        if(getIntent().hasExtra("channel")){
            Intent intent = getIntent();
            contentId = intent.getStringExtra("contentId");
            channel = intent.getStringExtra("channel");
            isMovie = channel.equals("movie");
            playMovieOrSeries(0);
        }
    }

    //播放剧集或者电影
    private void playMovieOrSeries(final int seriesIndex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(pgmList ==null || pgmList.length==0){
                    pgrpInfo = new PgrpAction().getPgmDetail(true, contentId, "zh");
                    if(pgrpInfo !=null) {
                        Log.e(TAG, "pgrpInfo : " + pgrpInfo.toString());
                        pgmList = pgrpInfo.getPgmList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(getIntent()!=null) setViewDatas(getIntent());
                            }
                        });
                    }
                }
                if(pgmList!=null && pgmList.length>0) {
                    final String[] sourceUrl;
                    if(pgmList.length>1){
                        sourceUrl = pgmList[seriesIndex].getPlayUrl();
                        setSerialName();
                        // 存入application
                        App.getInstance().setPgrpInfo(pgrpInfo);
                    }else {
                        sourceUrl = pgmList[0].getPlayUrl();
                    }
                    if(sourceUrl!=null && sourceUrl.length>0) {
                        for(int i=0;i<sourceUrl.length;i++){
                            final int index = i;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isDestroyed()){
                                        return;
                                    }
                                    GetPlayUrlTool getPlayUrlTool = new GetPlayUrlTool();
                                    getPlayUrlTool.getPlayUrl(sourceUrl[index]);
                                    final String playUrl = getPlayUrlTool.playUrlStr;
                                    final String userAgent = getPlayUrlTool.userAgent;
                                    if(!getRealPlayUrlSuccess && !TextUtils.isEmpty(playUrl)) {
                                        getRealPlayUrlSuccess=true;
                                        toastOnMainThread(pgmList[seriesIndex].getSourceCode()[index]+":获取地址成功");
                                        Log.e(TAG, "thread : " + Thread.currentThread() +"playUrl "+index+":"+playUrl);
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                tool = new PlayerInitAndPlayTool();
                                                tool.play(playUrl,userAgent, (VideoPlayerView) findViewById(R.id.video_details_view)
                                                        ,(RelativeLayout) findViewById(R.id.video_container));
                                                tool.setFinishListener(new PlayerInitAndPlayTool.finishListener() {
                                                    @Override
                                                    public void finish() {
                                                        Toast.makeText(PgmDetailActivity.this,"当前节目播放列表无可播放地址",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                tool.tvSerialIndex = tvSerialIndex;
                                            }
                                        });
                                    } else if(getRealPlayUrlSuccess && !TextUtils.isEmpty(playUrl)) {
                                        Log.e(TAG, "thread : " + Thread.currentThread() +"playUrl "+index+":"+playUrl);
                                        toastOnMainThread(pgmList[seriesIndex].getSourceCode()[index]+":获取地址成功");
                                        if(tool!=null && !TextUtils.isEmpty(playUrl)){
                                            tool.spareUrls.add(playUrl);
                                            tool.agents.add(userAgent);
                                        }
                                    } else {
                                        //获取的地址为空
                                        Log.e(TAG, "thread : " + Thread.currentThread() +" playUrl: "+index+":"+playUrl);
                                        Log.e(TAG, pgmList[seriesIndex].getSourceCode()[index] +" playUrl: " + playUrl);
                                        Log.e(TAG, "serialIndex:"+seriesIndex+" index:"+index);
                                        toastOnMainThread(pgmList[seriesIndex].getSourceCode()[index]+":获取地址为空");
                                    }
                                }
                            }).start();
                        }
                    }
                }
            }
        }).start();
    }

    private void setViewDatas(Intent intent) {
        video_detail_epgName.setText(intent.getStringExtra("pgrpName"));
        if (!StringUtils.isEmpty(pgrpInfo.getDirector())) {
            video_detail_dirertorName.setText(pgrpInfo.getDirector());
        }
        if (pgrpInfo.getActor() != null && pgrpInfo.getActor().length != 0) {
            String actor = Arrays.toString(pgrpInfo.getActor());
            actor = actor.substring(1, actor.length() - 1);
            actor = actor.replace("/", "、");
            video_detail_actorName.setText(actor);
        }
        // 显示tabs1中的详细介绍界面
        String type = pgrpInfo.getTypeCode();
        video_detail_typeName.setText(TextUtils.isEmpty(type)? getString(R.string.no_desc) : type);
        String publishTime = pgrpInfo.getYear();
        tv_publish_time.setText(TextUtils.isEmpty(publishTime)? getString(R.string.no_desc) : publishTime);
        if(!isDestroyed()) Glide.with(this).load(pgrpInfo.getLogo()).placeholder(R.drawable.epg_bg).into(video_detail_img);
        String desc = pgrpInfo.getDesc();
        description.setText("     " + (TextUtils.isEmpty(desc)? getString(R.string.no_desc) : desc));
        //显示剧集选择按钮
        if(pgmList.length>1){
            View seriesBtn = findViewById(R.id.seriesBtn);
            seriesBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setSerialName() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tvSerial = findViewById(R.id.tvSerial);
                tvSerial.setText(pgmList[tvSerialIndex].getName());
            }
        });
    }

    public void playVideo() {
        if (pgmList != null && pgmList.length > 0) {
            UIHelper.showSelectPgmSource(PgmDetailActivity.this, pgmList[tvSerialIndex], tvSerialIndex, pgrpInfo, isMovie);
        }
    }

    public ImageView getView() {
        return video_detail_img;
    }

    public void onRetry(View view) {
        BtnClickAnimTool.setBtnClkAnim(findViewById(R.id.iv_retry));
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tool!=null){
            tool.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(tool!=null){
            tool.release();
        }
    }

    public void jujiClk(View view) {
        if(popupView==null){
            popupView = new XPopup.Builder(view.getContext())
                    .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                    .asCustom(new JujiBottomPopup(PgmDetailActivity.this, pgmList));
        }
        popupView.show();
    }

    public void setSeriesIndex(int pos) {
        if(tool!=null){
            tool.release();
        }
        getRealPlayUrlSuccess = false;
        playMovieOrSeries(pos);
        tvSerialIndex = pos;
    }

    void toastOnMainThread(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PgmDetailActivity.this,str,Toast.LENGTH_LONG).show();
            }
        });
    }
}
