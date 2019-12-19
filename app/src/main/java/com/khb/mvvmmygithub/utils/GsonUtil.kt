package com.khb.mvvmmygithub.utils

import com.google.gson.Gson
import com.khb.mvvmmygithub.base.BaseApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 *创建时间：2019/11/28
 *编写人：kanghb
 *功能描述：
 */
object GsonUtil : KodeinAware {
    override val kodein: Kodein
        get() = BaseApplication.INSTANCE.kodein
    val INSTANCE: Gson by instance()
}

fun <T> T.toJson():String{
    return GsonUtil.INSTANCE.toJson(this)
}
inline fun <reified T> String.fromJson(): T{
    return GsonUtil.INSTANCE.fromJson(this,T::class.java)
}