package com.khb.mvvmmygithub.di

import com.khb.mvvmmygithub.BuildConfig
import com.khb.mvvmmygithub.http.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *创建时间：2019/11/27
 *编写人：kanghb
 *功能描述：创建retrofit相关的Kodein容器
 */

private const val HTTP_CLIENT_MODELU_TAG = "httpClientModule"

const val TIME_OUT_SECONDES = 10
const val BASE_URL = "https://api.github.com/"


val httpClientModule = Kodein.Module(HTTP_CLIENT_MODELU_TAG) {

    bind<Retrofit.Builder>() with provider { Retrofit.Builder() }

    bind<OkHttpClient.Builder>() with provider { OkHttpClient.Builder() }

    bind<HttpLoggingInterceptor>() with singleton {
        HttpLoggingInterceptor().apply {
            level = when (BuildConfig.DEBUG) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    bind<BasicAuthInterceptor>() with singleton {
        BasicAuthInterceptor(instance())
    }

    bind<OkHttpClient>() with singleton {
        instance<OkHttpClient.Builder>()
            .readTimeout(TIME_OUT_SECONDES.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_SECONDES.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_SECONDES.toLong(), TimeUnit.SECONDS)
            .addInterceptor(instance<HttpLoggingInterceptor>())
            .addInterceptor(instance<BasicAuthInterceptor>())
            .build()
    }

    bind<Retrofit>() with singleton {
        instance<Retrofit.Builder>()
            .baseUrl(BASE_URL)
            .client(instance())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
