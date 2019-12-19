package com.khb.mvvmlibrary.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

/**
 *创建时间：2019/11/14
 *编写人：kanghb
 *功能描述：委托属性方式封装Extras，支持在 AppCompatActivity 和 Fragment 中使用。
 */
class ExtrasDelegate<out T>(private val extraName: String, private val defaultValue: T) {
    private var extra: T? = null

    operator fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        extra = getExtra(extra, extraName, thisRef)
        return extra ?: defaultValue
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        extra = getExtra(extra, extraName, thisRef)
        return extra ?: defaultValue
    }

}
fun <T> extraDelegate(extra: String, default: T) = ExtrasDelegate(extra, default)

fun extraDelegate(extra: String) = extraDelegate(extra, null)

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: AppCompatActivity): T? =
    oldExtra ?: thisRef.intent?.extras?.get(extraName) as T?

@Suppress("UNCHECKED_CAST")
private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: Fragment): T? =
    oldExtra ?: thisRef.arguments?.get(extraName) as T?