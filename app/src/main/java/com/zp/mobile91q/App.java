package com.zp.mobile91q;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.zp.mobile91q.pgmdetail.PgrpInfo;
import com.zp.mobile91q.videoplayer.DeviceUtil;

public class App extends Application {
    private static App instance;
    private Context mContext;
    public App() {
    }

    private App(Context context) {
        if (context != null) {
            mContext = context;
        }
    }

    public synchronized static App getInstance() {
        if (null == instance) {
            instance = new App(null);
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        DeviceUtil.widthPixels = getResources().getDisplayMetrics().widthPixels;
        DeviceUtil.heightPixels = getResources().getDisplayMetrics().heightPixels;
    }

    private PgrpInfo pgrpInfo;

    public PgrpInfo getPgrpInfo() {
        return pgrpInfo;
    }

    public void setPgrpInfo(PgrpInfo pgrpInfo) {
        this.pgrpInfo = pgrpInfo;
    }

    public String getBBNum() {
        return "110000006";
    }
}
