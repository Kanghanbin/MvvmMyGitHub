package com.khb.mvvmmygithub.ui.main

import android.content.res.Configuration

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.MenuItem
import android.view.View

import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.khb.mvvmlibrary.adapter.ViewPagerAdapter
import com.khb.mvvmlibrary.base.view.fragment.BaseFragment
import com.khb.mvvmlibrary.ext.reactivex.clickThrottleFrist
import com.khb.mvvmmygithub.R



import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_main.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

@Suppress("PLUGIN_WARNING")
class MainFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_main
    override val kodein: Kodein = Kodein.lazy {
        extend(parentKodein)
        import(mainModule)
        bind<FragmentManager>() with instance(childFragmentManager)

    }
    private var isPortMode: Boolean = true

    private val listFragment : List<Fragment> by instance(MAIN_LIST_FRAGMENT)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPortMode = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        if (!isPortMode) bindPortScreen() else bindLandScreen()
        viewpager.adapter = ViewPagerAdapter(childFragmentManager, listFragment)
        viewpager.offscreenPageLimit = 2
    }

    private fun bindLandScreen() {
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit


            override fun onPageSelected(position: Int) {
                onPageSelect(position)
            }

        })

        bottom_nav.setOnNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(menuItem)
            true
        }

    }

    private fun onNavigationItemSelected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.item_home ->
                viewpager.currentItem = 0
            R.id.item_task ->
                viewpager.currentItem = 1
            R.id.item_mine ->
                viewpager.currentItem = 2
        }

    }


    private fun bindPortScreen() {
        Observable.mergeArray(
            fabHome.clickThrottleFrist().map { 0 },
            fabRepo.clickThrottleFrist().map { 1 },
            fabMine.clickThrottleFrist().map { 2 }
        ).autoDispose(scropeProvider)
            .subscribe(this::onPageSelect)
    }


    private fun onPageSelect(position: Int) {
        if (isPortMode) {
            for (i in 0..position) {
                if (bottom_nav.visibility == View.VISIBLE) {
                    bottom_nav.menu.getItem(i).isChecked = i == position
                }
            }
        } else {

            if (position != viewpager.currentItem) {
                viewpager.currentItem = position
                if (fam != null && fam.isExpanded) {
                    fam.collapse()
                }
            }
        }

    }

}
