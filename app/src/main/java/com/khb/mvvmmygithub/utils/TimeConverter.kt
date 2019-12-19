package com.khb.mvvmmygithub.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 *创建时间：2019/12/12
 *编写人：kanghb
 *功能描述：
 */
object TimeConverter {

    fun transTimeAgo(time: String?): String =
        transTimeStamp(time).let {
            DateUtils.getRelativeTimeSpanString(it).toString()
        }

    fun transTimeStamp(time: String?): Long =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).let {
            it.timeZone = TimeZone.getTimeZone("GMT+1")
            it.parse(time).time
        }
}