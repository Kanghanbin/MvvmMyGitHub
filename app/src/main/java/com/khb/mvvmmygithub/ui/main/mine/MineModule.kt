package com.khb.mvvmmygithub.ui.main.mine

import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/17
 *编写人：kanghb
 *功能描述：
 */
private const val MINE_MODULE_TAG = "mineModule"

val mineModule = Kodein.Module(MINE_MODULE_TAG) {

    bind<MineRepository>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        MineRepository(mineRemoteDataSourse = MineRemoteDataSourse(instance()))
    }

    bind<MineViewModel>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        MineViewModel.instance(context,instance())
    }
}
