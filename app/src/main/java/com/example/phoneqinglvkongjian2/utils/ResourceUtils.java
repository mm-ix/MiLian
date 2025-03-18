package com.example.phoneqinglvkongjian2.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import com.example.phoneqinglvkongjian2.R;

public class ResourceUtils {

    /**
     * 获取Drawable资源，如果资源不存在则返回默认资源
     * 
     * @param context 上下文
     * @param resId 资源ID
     * @return Drawable对象
     */
    public static Drawable getDrawableOrDefault(Context context, int resId) {
        try {
            return ContextCompat.getDrawable(context, resId);
        } catch (Exception e) {
            // 如果资源不存在，返回默认图标
            return ContextCompat.getDrawable(context, android.R.drawable.ic_menu_help);
        }
    }
    
    /**
     * 获取颜色资源，如果资源不存在则返回默认颜色
     * 
     * @param context 上下文
     * @param resId 资源ID
     * @return 颜色值
     */
    public static int getColorOrDefault(Context context, int resId) {
        try {
            return ContextCompat.getColor(context, resId);
        } catch (Exception e) {
            // 如果资源不存在，返回黑色
            return 0xFF000000;
        }
    }
} 