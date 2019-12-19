package com.khb.mvvmmygithub.ui.main.repo

import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/16
 *编写人：kanghb
 *功能描述：
 */

const val REPOS_MODULE_TAG = "reposModule"

val reposModule = Kodein.Module(REPOS_MODULE_TAG) {
    bind<ReposRepository>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        ReposRepository(
            local = LocalReposDataSourse(db = instance()),
            remote = RemoteReposDataSourse(serviceManager = instance())
        )
    }

    bind<ReposViewModel>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
        ReposViewModel.instance(fragment = context, reposRepository = instance())
    }
}