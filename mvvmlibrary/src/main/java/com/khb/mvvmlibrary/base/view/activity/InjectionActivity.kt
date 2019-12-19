package com.khb.mvvmlibrary.base.view.activity

import androidx.appcompat.app.AppCompatActivity
import com.khb.mvvmlibrary.base.view.IView
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.kcontext

/**
 *创建时间：2019/11/6
 *编写人：kanghb
 *功能描述：
 */
abstract class InjectionActivity : AutoDisposeActivity(), KodeinAware, IView {
    protected val parentKodein by closestKodein()

    override val kodeinContext = kcontext<AppCompatActivity>(this)

    override val kodein: Kodein by retainedKodein {
        extend(parentKodein, copy = Copy.All)
    }

}
