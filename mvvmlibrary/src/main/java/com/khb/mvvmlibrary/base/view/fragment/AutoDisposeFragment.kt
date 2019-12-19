package com.khb.mvvmlibrary.base.view.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

/**
 *创建时间：2019/11/6
 *编写人：kanghb
 *功能描述：
 */
abstract class AutoDisposeFragment : Fragment(){

    protected val scropeProvider :AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(this,Lifecycle.Event.ON_DESTROY)
    }
}