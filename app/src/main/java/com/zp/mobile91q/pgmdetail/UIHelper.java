
package com.zp.mobile91q.pgmdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zp.mobile91q.App;
import com.zp.mobile91q.R;
import com.zp.mobile91q.videoplayeractivity.VideoPlayerActivity;
import com.zp.mobile91q.category.MetaDataAction;
import com.zp.mobile91q.category.WebsiteInfo;
import com.zp.mobile91q.pgmdetail.PgrpInfo.PgmInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 做dialog 显示，Activity跳转，toast显示。
 */
public class UIHelper {
    private static MetaDataAction mMetaDataAction;
    private static Map<String, WebsiteInfo> sourceMap = new HashMap<String, WebsiteInfo>();
    public static String channel = "";
    public static String contentId = "";

    /**
     * 点击视频，弹出对话框，列出视频来源，本地播放或上传TV，
     */
    public static void showSelectPgmSource(final PgmDetailActivity context,
            final PgmInfo pgmInfo, int index, final PgrpInfo pgrpInfo, boolean isMovie) {
        // 存入application
        App.getInstance().setPgrpInfo(pgrpInfo);
        final boolean ifPushHistory = false;
        final String[] sources = pgmInfo.getSourceCode();
        final String[] sourceUrls = pgmInfo.getPlayUrl();
        final int programIndex = index;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.epg_source_dialog, null);
        ListView list = (ListView) view.findViewById(R.id.epg_dialog_list);
        final PopupWindow dialog = new PopupWindow(view, (width / 3) * 2, (width / 3) * 2);
        EPGSourceSelectAdapter adapter = new EPGSourceSelectAdapter(context,
                getSourceName(sources), false,
                new EPGSourceSelectAdapter.ComponentSelectListener() {

                    @Override
                    // 来源选择，点击本地播放
                    public void onTextViewSelect(int index) {
                        String sourceUrl = sourceUrls[index];
                        // 跳转到播放界面
                        UIHelper.showPgmPlayActivity(context, sourceUrl, programIndex, index, ifPushHistory);
                        dialog.dismiss();
                    }

                    @Override
                    // 点击TV去播放
                    public void onButtonSelect_fly(int index) {
                    }

                    @Override
                    // 点击推送缓存
                    public void onButtonSelect_local(int index) {
                    }
                });
        list.setAdapter(adapter);
        // 设置背景，不然无法响应返回键
        ColorDrawable dw = new ColorDrawable(0x00000000);
        dialog.setBackgroundDrawable(dw);
        // 点击外部消失
        dialog.setOutsideTouchable(true);
        // 设置不让gridview收不到监听
        dialog.setFocusable(true);
        dialog.update();
        //设置屏幕中间位置
        dialog.showAsDropDown(context.getView(),width/6-24,-50);
//        setBackgroundAlpha(context,0.5f);
    }

    public static String[] getSourceName(String[] sourceCode) {
        mMetaDataAction = MetaDataAction.getinstance();
        sourceMap = mMetaDataAction.getWebsiteMap();
        String[] sourceName = new String[sourceCode.length];
        for (int i = 0; i < sourceCode.length; i++) {
            if (sourceMap != null && sourceMap.get(sourceCode[i]) != null) {
            	//agMj判断一下，用中文替换
                if(TextUtils.equals("agMj", sourceMap.get(sourceCode[i]).getName())){
                	String temp = "阿哥美劇";
                	sourceName[i] = temp;
                } else{
                	sourceName[i] = sourceMap.get(sourceCode[i]).getName();
                }
            } else {
                sourceName[i] = "unknown";
            }
        }
        return sourceName;
    }

    /**
     * 跳转到播放界面
     *
     * @param context
     */
    public static void showPgmPlayActivity(Context context, String sourceUrl, int serialId, int sourcesID,
            boolean ifPushHistory) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("sourceUrl", sourceUrl);
        intent.putExtra("sourcesID", sourcesID);
        intent.putExtra("serialId", serialId);
        intent.putExtra("ifItHasHistory", ifPushHistory);
        intent.putExtra("seekPosition", -1);// 避免有历史记录时走到播放流程
        context.startActivity(intent);
    }

    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
