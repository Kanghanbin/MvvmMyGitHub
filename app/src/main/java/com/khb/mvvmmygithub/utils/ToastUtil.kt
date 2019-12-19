package com.khb.mvvmmygithub.utils

import com.khb.mvvmlibrary.ext.toast
import com.khb.mvvmmygithub.base.BaseApplication

/**
 *创建时间：2019/12/4
 *编写人：kanghb
 *功能描述：
 */
inline fun toast(value: () -> String): Unit {
    BaseApplication.INSTANCE.toast(value)
}