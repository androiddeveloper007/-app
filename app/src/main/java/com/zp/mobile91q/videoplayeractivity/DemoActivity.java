package com.zp.mobile91q.VideoPlayerActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

    }

}
