package com.khb.mvvmlibrary.ext

import android.content.Context
import android.widget.Toast

/**
 *创建时间：2019/12/4
 *编写人：kanghb
 *功能描述：
 */
fun Context.toast(value: String) = toast { value }

inline fun Context.toast(value: () -> String)  =
    Toast.makeText(this, value(), Toast.LENGTH_SHORT).show()

