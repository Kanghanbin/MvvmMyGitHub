package com.khb.mvvmlibrary.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *创建时间：2019/12/2
 *编写人：kanghb
 *功能描述：
 */
object RxSchedulers {
    val database: Scheduler get() = Schedulers.single()

    val io: Scheduler get() = Schedulers.io()

    val ui: Scheduler get() = AndroidSchedulers.mainThread()
}