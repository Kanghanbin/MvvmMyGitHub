package com.khb.mvvmmygithub.common

import androidx.recyclerview.widget.RecyclerView
import com.khb.mvvmlibrary.util.RxSchedulers
import com.khb.mvvmmygithub.http.Errors
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.zipWith

/**
 *创建时间：2019/12/12
 *编写人：kanghb
 *功能描述：
 */
val scrollChangedObservableTransformer: ObservableTransformer<Int, Boolean>
    get() = ObservableTransformer { upstream ->
        upstream.map { it == RecyclerView.SCROLL_STATE_IDLE }
            .compose { upstream ->
                upstream.zipWith(upstream.startWith(true)) { last, current ->

                    when (last == current) {
                        true -> Errors.EmptyInputError
                        false -> current
                    }

                }
            }.flatMap {
                when (it) {
                    is Boolean -> Observable.just(it)
                    else -> Observable.empty<Boolean>()
                }
            }
            .observeOn(RxSchedulers.ui)
    }