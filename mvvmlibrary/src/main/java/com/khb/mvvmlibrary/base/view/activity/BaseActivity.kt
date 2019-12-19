package com.khb.mvvmlibrary.base.view.activity

import android.os.Bundle

/**
 *创建时间：2019/11/6
 *编写人：kanghb
 *功能描述：
 */
abstract class BaseActivity : InjectionActivity() {

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

}