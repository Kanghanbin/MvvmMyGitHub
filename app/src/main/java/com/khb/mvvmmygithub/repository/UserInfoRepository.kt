package com.khb.mvvmmygithub.repository

import android.content.SharedPreferences
import com.khb.mvvmlibrary.util.SingletonHolder
import com.khb.mvvmlibrary.util.prefs.boolean
import com.khb.mvvmlibrary.util.prefs.string

/**
 *创建时间：2019/11/29
 *编写人：kanghb
 *功能描述：
 */
class UserInfoRepository(sharedPreferences: SharedPreferences){

    var accessToken :String by sharedPreferences.string("user_access_token","")

    var username by sharedPreferences.string("username","")

    var password by sharedPreferences.string("password","")

    var isAutoLogin by sharedPreferences.boolean("auto_login",true)

    companion object :
        SingletonHolder<UserInfoRepository, SharedPreferences>(::UserInfoRepository)
}