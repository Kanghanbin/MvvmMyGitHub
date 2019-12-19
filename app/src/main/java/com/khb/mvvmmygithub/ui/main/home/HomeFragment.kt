package com.khb.mvvmmygithub.ui.main.home

import android.animation.ObjectAnimator
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.khb.mvvmlibrary.base.view.fragment.BaseFragment
import com.khb.mvvmlibrary.ext.reactivex.clickThrottleFrist
import com.khb.mvvmlibrary.util.RxSchedulers

import com.khb.mvvmmygithub.R
import com.khb.mvvmmygithub.base.BaseApplication
import com.khb.mvvmmygithub.common.scrollChangedObservableTransformer
import com.khb.mvvmmygithub.utils.jumpBrowser
import com.khb.mvvmmygithub.utils.toast
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class HomeFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_home

    override val kodein: Kodein = Kodein.lazy {
        extend(parentKodein)
        import(homeModule)
    }
    private val viewModel: HomeViewModel by instance()

    private val pageAdapter: HomePageAdapter = HomePageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binds()
        rvHome.adapter = pageAdapter
    }

    private fun binds() {
        rvHome.scrollStateChanges()
            //debounce操作符会被新来的冲掉，然后重新计算时间timeout
            .debounce(500, TimeUnit.MILLISECONDS)
            .compose(scrollChangedObservableTransformer)
            .autoDispose(scropeProvider)
            //双冒号骚操作，只能当做参数来用，不能直接用哦
            .subscribe(::switchFabState)

        fabTop.clickThrottleFrist()
            .map { 0 }
            .autoDispose(scropeProvider)
            .subscribe(rvHome::scrollToPosition)

        mSwipeRefreshLayout.refreshes()
            .autoDispose(scropeProvider)
            .subscribe { viewModel.refreshRemoteDataSourse() }

        pageAdapter.getItemClickEvent()
            .autoDispose(scropeProvider)
            .subscribe(BaseApplication.INSTANCE::jumpBrowser)

        viewModel.observeViewState()
            .observeOn(RxSchedulers.ui)
            .autoDispose(scropeProvider)
            .subscribe(::onStateArrived)
    }

    private fun onStateArrived(homeviewState: HomeViewState) {
        if (homeviewState.throwable != null) {
            toast { homeviewState.throwable.message ?: "network error." }
        }
        if (homeviewState.pagedList != null) {
            pageAdapter.submitList(homeviewState.pagedList)
        }
        if (homeviewState.isLoading != mSwipeRefreshLayout.isRefreshing) {
            mSwipeRefreshLayout.isRefreshing = homeviewState.isLoading
        }

    }

    private fun switchFabState(show: Boolean) {
        when (show) {
            false -> ObjectAnimator.ofFloat(fabTop, "translationX", 250f, 0f)
            true -> ObjectAnimator.ofFloat(fabTop, "translationX", 0f, 250f)
        }.apply {
            duration = 500
            start()
        }
    }

}
