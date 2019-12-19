package com.khb.mvvmmygithub.ui.login

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/5
 *编写人：kanghb
 *功能描述：
 */

private const val LOGIN_MODULE_TAG = "loginModule"

val loginModule = Kodein.Module(LOGIN_MODULE_TAG) {
    bind<LoginViewModel>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        ViewModelProviders.of((context).activity!!, LoginViewModelFactory(instance()))
            .get(LoginViewModel::class.java)
    }

    bind<LoginRemoteDataSourse>() with singleton {
        LoginRemoteDataSourse(instance())
    }

    bind<LoginLocalDataSourse>() with singleton {
        LoginLocalDataSourse(instance(), instance())
    }

    bind<LoginRepository>() with singleton {
        LoginRepository(instance(), instance())
    }
}