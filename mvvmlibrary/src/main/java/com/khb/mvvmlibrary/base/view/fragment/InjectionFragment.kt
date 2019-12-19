package com.khb.mvvmlibrary.base.view.fragment

import androidx.fragment.app.Fragment
import com.khb.mvvmlibrary.base.view.IView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.kcontext

/**
 *创建时间：2019/11/6
 *编写人：kanghb
 *功能描述：
 */
abstract class InjectionFragment : AutoDisposeFragment(), KodeinAware, IView {

    val parentKodein: Kodein by closestKodein()

    override val kodeinContext = kcontext<Fragment>(this)
}