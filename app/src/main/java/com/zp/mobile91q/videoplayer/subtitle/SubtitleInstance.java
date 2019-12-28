
package com.zp.mobile91q.videoplayer.subtitle;

import android.text.TextUtils;
import android.util.Log;

/**
 * 字幕解析入口
 * 
 * @author Jay
 * @date 2016.7.18
 */
public class SubtitleInstance {
    private static final String TAG = "SubtitleInstance";

    public SubtitleInstance() {

    }

    /**
     * @param filePath subtitle file path;
     * @return parsed AbstractParser.
     * @description 根据传入的字幕路径来实例化不同类型的字幕解析类
     */
    public AbstractParser parseSubtitleFile(String filePath) {
        Log.e(TAG, "parseSubtitleFile path: " + filePath);
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        AbstractParser ap = null;
        SUBTYPE type = SubtitleUtils.getSubType(filePath);
        Log.i(TAG, "type=" + type.toString());
        switch (type) {
            case SUB_SRT:
                ap = new SrtParser();
                ap.parse(filePath);
                break;
            case SUB_SSA:
            case SUB_ASS:
                ap = new SsaParser();
                ap.parse(filePath);
                break;
            case SUB_SMI:
                ap = new SmiParser();
                ap.parse(filePath);
                break;
            case SUB_IDXSUB:
                // TODO
                break;
            default:
                break;
        }
        return ap;
    }
}
