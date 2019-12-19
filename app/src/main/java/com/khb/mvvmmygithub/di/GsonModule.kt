package com.khb.mvvmmygithub.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/11/28
 *编写人：kanghb
 *功能描述：
 */
private const val GSON_MODULE_TAG = "GSONMODELE"

val gsonModule = Kodein.Module(GSON_MODULE_TAG){
    bind<Gson>() with singleton{
        GsonBuilder().create()
    }
}