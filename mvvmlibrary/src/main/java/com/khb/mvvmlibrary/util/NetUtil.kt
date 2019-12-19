package com.khb.mvvmlibrary.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

/**
 *创建时间：2019/11/7
 *编写人：kanghb
 *功能描述：网络工具类
 */
fun Context.isNetworkAvailble(): Boolean = applicationContext.isNetworkAvailble()

fun Application.isNetworkAvailable(): Boolean = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    .activeNetworkInfo.isConnected