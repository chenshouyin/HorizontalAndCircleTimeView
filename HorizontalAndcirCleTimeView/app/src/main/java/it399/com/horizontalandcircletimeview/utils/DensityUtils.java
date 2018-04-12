package it399.com.horizontalandcircletimeview.utils;

import android.content.Context;

/**
 * Created by shouyinchen on 2018/1/18.
 *
 * 在线助手:http://www.it399.com/
 *
 * Github:https://github.com/chenshouyin
 *
 *CSDN博客:https://blog.csdn.net/e_inch_photo
 *
 * DensityUtils
 */

public class DensityUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
