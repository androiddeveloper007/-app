package com.zp.mobile91q.loadingview;

import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.zp.mobile91q.R;

public class PlaceholderHelper {
    private PlaceholderHelper() {
        throw new UnsupportedOperationException("Can not be instantiated.");
    }
    public static PlaceholderParameter getParameter(View view) {
        if (view == null) {
            return null;
        }
        int placeHolderColor = Color.parseColor("#dddddd");

        switch (view.getId()) {
            case R.id.media_info_text:
                Animation locationAnimation = new ScaleAnimation(0.1f, 0.5f, 0.1f, 0.5f);
                locationAnimation.setDuration(800);
                locationAnimation.setRepeatMode(Animation.REVERSE);
                locationAnimation.setRepeatCount(Animation.INFINITE);
                return new PlaceholderParameter.Builder()
                        .setView(view)
                        .setAnimation(locationAnimation)
                        .setDrawable(DrawableUtils.createRectangleDrawable(placeHolderColor, 5))
                        .build();
            case R.id.media_info_image:
                Animation scaleAnimation = new ScaleAnimation(0.1f, 0.5f, 0.1f, 0.5f, Animation.RELATIVE_TO_SELF ,0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(800);
                scaleAnimation.setRepeatMode(Animation.REVERSE);
                scaleAnimation.setRepeatCount(Animation.INFINITE);
                return new PlaceholderParameter.Builder()
                        .setView(view)
                        .setAnimation(scaleAnimation)
                        .setDrawable(DrawableUtils.createOvalDrawable(placeHolderColor))
                        .build();
        }
        return null;
    }
}
