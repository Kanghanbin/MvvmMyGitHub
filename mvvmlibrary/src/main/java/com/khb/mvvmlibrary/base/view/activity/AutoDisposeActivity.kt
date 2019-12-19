package com.khb.mvvmlibrary.base.view.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

/**
 *创建时间：2019/11/5
 *编写人：kanghb
 *功能描述：
 */
abstract class AutoDisposeActivity : AppCompatActivity() {
    protected val scropeProvider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)
    }
}