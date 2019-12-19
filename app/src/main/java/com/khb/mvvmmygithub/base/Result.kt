package com.khb.mvvmmygithub.base

import com.khb.mvvmlibrary.base.viewstate.IViewState

/**
 *创建时间：2019/12/5
 *编写人：kanghb
 *功能描述：一个密封状态类，封装了返回结果
 */
sealed class Result<out T> : IViewState {
    object Loading : Result<Nothing>()
    object Idle : Result<Nothing>()
    data class Failure(val error: Throwable) : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()

    companion object {
        fun <T> loading(): Result<T> = Loading
        fun <T> idle(): Result<T> = Idle
        fun <T> failue(error: Throwable): Result<T> = Failure(error)
        fun <T> success(data: T): Result<T> = Success(data)
    }
}