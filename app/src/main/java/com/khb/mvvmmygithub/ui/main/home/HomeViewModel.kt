package com.khb.mvvmmygithub.ui.main.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.khb.mvvmlibrary.base.viewmodel.BaseViewModel
import com.khb.mvvmlibrary.ext.reactivex.copyMap
import com.khb.mvvmmygithub.base.Result
import com.uber.autodispose.autoDispose
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 *创建时间：2019/12/10
 *编写人：kanghb
 *功能描述：
 */
class HomeViewModel(
    private val repo: HomeRepository
) : BaseViewModel() {
    //HomeViewModel内部使用，首次进来刷新
    private val refreshStateChangedEventSubject = BehaviorSubject.create<Boolean>()
    //供HomeFragment监听HomeViewState
    private val homeViewStateSubject = BehaviorSubject.createDefault(HomeViewState.initial())

    init {
        refreshStateChangedEventSubject
            .distinctUntilChanged()
            .filter { it }
            .autoDispose(this)
            .subscribe { refreshRemoteDataSourse() }

        repo.subscribeRemoteRequestState()
            .autoDispose(this)
            .subscribe { result ->
                when (result) {
                    is Result.Loading -> homeViewStateSubject.copyMap { viewState ->
                        viewState.copy(isLoading = true, throwable = null)
                    }
                    is Result.Idle -> homeViewStateSubject.copyMap { viewState ->
                        viewState.copy(isLoading = false, throwable = null)
                    }
                    is Result.Failure -> homeViewStateSubject.copyMap { viewState ->
                        viewState.copy(isLoading = false, throwable = result.error)
                    }
                    is Result.Success -> homeViewStateSubject.copyMap { viewState ->
                        viewState.copy(isLoading = false, throwable = null)
                    }
                }
            }

        repo.initPagedListFromDb()
            .autoDispose(this)
            .subscribe { pagedList ->
                homeViewStateSubject.copyMap { homeViewState ->
                    homeViewState.copy(isLoading = false, throwable = null, pagedList = pagedList)
                }
            }

    }
    //外部HomeFragment调用
    fun observeViewState(): Observable<HomeViewState> {
        return homeViewStateSubject.hide().distinctUntilChanged()
    }

    fun refreshRemoteDataSourse() {
        repo.refreshPagedList()
    }

    override fun onCleared() {
        super.onCleared()
        repo.autoDisposeObserver.onNext(Unit)
        repo.autoDisposeObserver.onComplete()
    }

    companion object {
        fun instance(fragment: Fragment,repo: HomeRepository):HomeViewModel =
            ViewModelProviders.of(fragment,HomeViewModelFactory(repo))
                .get(HomeViewModel::class.java)
    }

    class HomeViewModelFactory(private val repo: HomeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(repo ) as T
        }

    }
}