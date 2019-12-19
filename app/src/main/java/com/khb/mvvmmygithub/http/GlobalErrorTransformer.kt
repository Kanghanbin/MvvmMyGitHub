package com.khb.mvvmmygithub.http

import com.khb.mvvmmygithub.responseentity.UserInfo
import com.khb.mvvmmygithub.utils.toast
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import org.reactivestreams.Publisher
import retrofit2.HttpException

/**
 *创建时间：2019/12/4
 *编写人：kanghb
 *功能描述：处理Observable onerror时调用
 */
class GlobalErrorTransformer<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Flowable<T> {

        return upstream.doOnError {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        401 -> toast { "401 Unauthorized" }
                        404 -> toast { "404 failure" }
                        500 -> toast { "500 server failure" }
                        else -> toast { "network failure" }
                    }

                }
                else -> toast { "network failure" }
            }
        }

    }


}