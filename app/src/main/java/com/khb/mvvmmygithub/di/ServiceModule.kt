package com.khb.mvvmmygithub.di

import com.khb.mvvmmygithub.http.ServiceManager
import com.khb.mvvmmygithub.http.UserService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

/**
 *创建时间：2019/11/27
 *编写人：kanghb
 *功能描述：创建service相关的Kodein容器
 */

private const val SERVICE_MODULE = "servicemodule"

val serviceModule = Kodein.Module(SERVICE_MODULE) {
    bind<UserService>() with singleton {
        instance<Retrofit>().create(UserService::class.java)
    }
    bind<ServiceManager>() with singleton {
        ServiceManager(instance())
    }
}