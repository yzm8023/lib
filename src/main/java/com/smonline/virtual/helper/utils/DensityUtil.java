package com.smonline.virtual.helper.utils;


import android.content.res.Resources;

import com.smonline.virtual.client.core.VirtualCore;

/**
 * Created by Administrator on 2017/5/3.
 */

public final class DensityUtil {

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth() {
        return VirtualCore.get().getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight() {
        return VirtualCore.get().getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(){
        int result = 0;
        Resources res = VirtualCore.get().getContext().getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dp2px(float dp) {
        float scale = VirtualCore.get().getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = VirtualCore.get().getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        float scale = VirtualCore.get().getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */

    public static int px2sp(float pxValue) {
        final float fontScale = VirtualCore.get().getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
