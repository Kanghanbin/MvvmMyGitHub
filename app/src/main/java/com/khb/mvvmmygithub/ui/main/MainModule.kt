package com.khb.mvvmmygithub.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.khb.mvvmmygithub.ui.main.home.HomeFragment
import com.khb.mvvmmygithub.ui.main.mine.MineFragment
import com.khb.mvvmmygithub.ui.main.repo.ReposFragment

import kotlinx.android.synthetic.main.fragment_main.*
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/6
 *编写人：kanghb
 *功能描述：
 */
private const val MAIN_MODULE_TAG = "mainModule"
const val MAIN_LIST_FRAGMENT = "mainListFragment"

val mainModule = Kodein.Module(MAIN_MODULE_TAG) {

    bind<HomeFragment>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        HomeFragment()
    }
    bind<ReposFragment>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        ReposFragment()
    }
    bind<MineFragment>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        MineFragment()
    }
    bind<BottomNavigationView>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        (context as MainFragment).bottom_nav
    }
    bind<ViewPager>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        (context as MainFragment).viewpager
    }

    bind<List<Fragment>>(MAIN_LIST_FRAGMENT) with scoped<Fragment>(AndroidLifecycleScope).singleton {
        listOf<Fragment>(
            instance<HomeFragment>(),
            instance<ReposFragment>(),
            instance<MineFragment>()
        )
    }
}
