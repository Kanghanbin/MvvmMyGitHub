package com.khb.mvvmmygithub.di

import android.content.Context
import android.content.SharedPreferences
import com.khb.mvvmmygithub.base.BaseApplication
import com.khb.mvvmmygithub.repository.UserInfoRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/5
 *编写人：kanghb
 *功能描述：
 */

private const val REPOSITORY_MODULE_TAG = "repositoryModule"

private const val SP_NAME_TAG = "spName"
val repositoryModule = Kodein.Module(REPOSITORY_MODULE_TAG){

    bind<SharedPreferences>(SP_NAME_TAG) with singleton {
        BaseApplication.INSTANCE.getSharedPreferences(SP_NAME_TAG,Context.MODE_PRIVATE)
    }
    bind<UserInfoRepository>() with singleton {
        UserInfoRepository.getInstance(instance(SP_NAME_TAG))
    }
}