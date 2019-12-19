package com.khb.mvvmmygithub.base

import android.app.Application
import android.content.Context
import com.khb.mvvmmygithub.di.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/11/7
 *编写人：kanghb
 *功能描述：
 */
class BaseApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<Context>() with singleton { this@BaseApplication }
        import(androidModule(this@BaseApplication))
        import(androidXModule(this@BaseApplication))
        import(httpClientModule)
        import(serviceModule)
        import(gsonModule)
        import(repositoryModule)
        import(dbModule)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
    }

}