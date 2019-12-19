package com.khb.mvvmmygithub.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URI

/**
 *创建时间：2019/12/12
 *编写人：kanghb
 *功能描述：
 */

fun Context.jumpBrowser(url: String) {
    val uri = Uri.parse(url)
    Intent(Intent.ACTION_VIEW, uri).let {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(it)
    }

}