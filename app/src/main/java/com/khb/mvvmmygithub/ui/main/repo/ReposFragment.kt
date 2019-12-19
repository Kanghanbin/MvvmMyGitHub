package com.khb.mvvmmygithub.ui.main.repo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_repos.*

import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class ReposFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_repos
    override val kodein: Kodein = Kodein.lazy {
        extend(parentKodein)
        import(reposModule)
    }

    private val reposViewModel: ReposViewModel by instance()
    private val pagedAdapter :ReposPagedAdapter = ReposPagedAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.menu_repos_filter_type)
        rvRepos.adapter = pagedAdapter
        binds()
    }

    private fun binds() {
        rvRepos.scrollStateChanges().debounce(500, TimeUnit.MILLISECONDS)
            .compose(scrollChangedObservableTransformer)
            .autoDispose(scropeProvider)
            .subscribe(::switchFabState)
        mSwipeRefreshLayout.refreshes()
            .flatMapCompletable { reposViewModel.refreshDataSourse() }
            .autoDispose(scropeProvider)
            .subscribe()

        fabTop.clickThrottleFrist()
            .map { 0 }
            .autoDispose(scropeProvider)
            .subscribe(rvRepos::scrollToPosition)

        toolbar.setOnMenuItemClickListener {
            onMenuSelectd(it)
            true
        }
        pagedAdapter.getItemClickEvent()
            .autoDispose(scropeProvider)
            .subscribe(BaseApplication.INSTANCE::jumpBrowser)

        reposViewModel.observeViewState()
            .observeOn(RxSchedulers.ui)
            .autoDispose(scropeProvider)
            .subscribe(::onNewState)
    }

    private fun onNewState(reposViewState: ReposViewState){
        if(reposViewState.throwable != null){
            toast { "network failure" }
        }
        if(reposViewState.isLoading != mSwipeRefreshLayout.isRefreshing){
            mSwipeRefreshLayout.isRefreshing = reposViewState.isLoading
        }
        if(reposViewState.pagedList != null){
            pagedAdapter.submitList(reposViewState.pagedList)
        }
    }

    private fun onMenuSelectd(menuItem: MenuItem) {
        reposViewModel.sortChanged(
            when (menuItem.itemId) {
                R.id.menu_repos_created -> ReposViewModel.sortByCreated
                R.id.menu_repos_letter -> ReposViewModel.sortByLetter
                R.id.menu_repos_update -> ReposViewModel.sortByUpdate
                else -> throw IllegalArgumentException("failure menuItem id.")
            }
        )

    }

    private fun switchFabState(show: Boolean) {
        when (show) {
            false -> ObjectAnimator.ofFloat(fabTop, "translationX", 250f, 0f)
            true -> ObjectAnimator.ofFloat(fabTop, "translationX", 0f, 250f)
        }.apply {
            duration = 300
            start()
        }
    }

}
