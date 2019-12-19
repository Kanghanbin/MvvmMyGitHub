package com.khb.mvvmlibrary.ext.reactivex

import io.reactivex.subjects.BehaviorSubject

/**
 *创建时间：2019/12/4
 *编写人：kanghb
 *功能描述：
 */
inline fun <reified T> BehaviorSubject<T>.copyMap(map: (T) -> T) {

    val oldValue: T? = value
    if (oldValue != null) {
        onNext(map(oldValue))
    } else {
        throw NullPointerException("BehaviorSubject<${T::class.java}> not contain value.")
    }
}

inline fun <reified T> BehaviorSubject<T>.copyFlatMap(map: (T) -> List<T>) {

    val oldValue: T? = value
    if (oldValue != null) {
        map(oldValue).forEach { onNext(it) }
    } else {
        throw NullPointerException("BehaviorSubject<${T::class.java}> not contain value.")
    }
}