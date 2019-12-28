package com.zp.mobile91q.pgmdetail;

import android.util.Log;

import com.zp.mobile91q.videoplayer.PolymerizePlayUrl;
import com.zp.mobile91q.videoplayer.StringUtils;

public class GetPlayUrlTool {
    public String playUrlStr = "";
    public String userAgent = "";
    public void getPlayUrl(String url) {
        PolymerizePlayUrl playUrl = new PolymerizePlayUrl();
        String url2 = playUrl.getDemand(url, 1);
        if (!StringUtils.isEmpty(url2)) {
            String[] list = url2.split("sciflyku");
            if (list.length > 1) {
                playUrlStr = list[0];
                userAgent = list[1];
            } else if (list.length == 1) {
                playUrlStr = list[0];
                userAgent = "";
            }
        } else {
            //获取地址为空时
            Log.e("ZZP", "result url： is empty  "+Thread.currentThread());
//            Log.e("ZZP", "playUrlStr： " + playUrlStr);
        }
    }
}
