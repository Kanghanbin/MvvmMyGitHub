package com.khb.mvvmlibrary.util

import android.annotation.SuppressLint
import android.content.Context

/**
 *创建时间：2019/11/7
 *编写人：kanghb
 *功能描述：
 */
object DisplayUtil {

    @SuppressLint("PrivateApi")
    fun getStatusbarHeight(context: Context): Int {
        var statusbarHeight = 0
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val o = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field.get(o) as Int
            statusbarHeight = context.resources.getDimensionPixelOffset(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusbarHeight

    }

    fun getScreenHeightExcludeStatusBar(context: Context): Int {
        return context.resources.displayMetrics.heightPixels - getStatusbarHeight(context)
    }

    fun px2dp(context: Context, pxValue: Float): Int {
        val density = context.resources.displayMetrics.density
        return (pxValue / density + 0.5).toInt()
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dpValue * density + 0.5).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (pxValue / scaledDensity + 0.5).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (spValue * scaledDensity + 0.5).toInt()
    }
}