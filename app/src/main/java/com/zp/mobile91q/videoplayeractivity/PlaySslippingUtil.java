
package com.zp.mobile91q.videoplayeractivity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class PlaySslippingUtil {

    private Context mContext;

    // 系统声音实例化
    private AudioManager mAudioManager;

    private int maxVoice;

    public PlaySslippingUtil(Context context) {
        mContext = context;
        // 声音
        mAudioManager = (AudioManager) (mContext.getSystemService(Context.AUDIO_SERVICE));
        // 最大音量获取
        maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    public float getCurrentBrightness() {
        float screenBrightness = 1.0f;
        screenBrightness = getCurrentBrite();
        Log.e("PlaySslippingUtil","getCurrentBrightness:"+screenBrightness);
        return screenBrightness;
    }

    //获取屏幕当前亮度
    public float getCurrentBrite() {
        float currentBrite = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
        return currentBrite;
    }

    public int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * 保存当前屏幕亮度值 0--255
     */
    public void saveScreenBrightness(float paramInt) {
        Log.e("PlaySslippingUtil","saveScreenBrightness:"+paramInt);
        setCurrentBrite(paramInt);
    }

    //设置屏幕亮度
    public void setCurrentBrite(float currentBrite) {
        Window mWindow = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.screenBrightness = currentBrite;
        mWindow.setAttributes(params);
    }

    /**
     * 最大声音获取
     */
    public int getMaxVoice() {
        return maxVoice;
    }

    public int getSysVoice() {
        Log.e("PlaySslippingUtil","getSysVoice:"+mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    /**
     * 系统实例化声音获取
     */
    public AudioManager getmAudioManager() {
        return mAudioManager;
    }

    public int getSysBright() {
        int bri = 0;
        try {
            bri = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return bri;
    }
}
