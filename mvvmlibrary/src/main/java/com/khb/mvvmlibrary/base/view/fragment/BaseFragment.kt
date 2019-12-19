package com.khb.mvvmlibrary.base.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *创建时间：2019/11/6
 *编写人：kanghb
 *功能描述：
 */
abstract class BaseFragment : InjectionFragment() {

    private var mRootView: View? = null

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(layoutId,container,false)
        return mRootView!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRootView = null
    }
}