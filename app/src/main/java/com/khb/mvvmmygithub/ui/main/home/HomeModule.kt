package com.khb.mvvmmygithub.ui.main.home

import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/9
 *编写人：kanghb
 *功能描述：
 */
const val HOME_MODULE_TAG = "homeModule"

val homeModule = Kodein.Module(HOME_MODULE_TAG) {
    bind<HomeViewModel>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        HomeViewModel.instance(
            fragment = context,
            repo = instance()
        )
    }
    bind<HomeRemoteDataSourse>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        HomeRemoteDataSourse(serviceManager = instance())
    }
    bind<HomeLocalDataSourse>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        HomeLocalDataSourse(db = instance())
    }

    bind<HomeRepository>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        HomeRepository(localDataSourse = instance(),remoteDataSourse = instance())
    }
}