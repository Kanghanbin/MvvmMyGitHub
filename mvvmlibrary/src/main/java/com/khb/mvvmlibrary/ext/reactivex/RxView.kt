package com.khb.mvvmlibrary.ext.reactivex

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 *创建时间：2019/11/11
 *编写人：kanghb
 *功能描述：
 */
fun View.clickThrottleFrist(): Observable<Unit> {
    return clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
}