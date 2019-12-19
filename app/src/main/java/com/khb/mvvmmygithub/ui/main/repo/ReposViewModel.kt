package com.khb.mvvmmygithub.ui.main.repo

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.khb.mvvmlibrary.base.viewmodel.BaseViewModel
import com.khb.mvvmlibrary.ext.reactivex.copyMap
import com.khb.mvvmlibrary.util.RxSchedulers
import com.khb.mvvmmygithub.base.Result
import com.uber.autodispose.autoDispose
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 *创建时间：2019/12/16
 *编写人：kanghb
 *功能描述：
 */

class ReposViewModel(private val reposRepository: ReposRepository) : BaseViewModel() {

    private val mViewStateSubject: BehaviorSubject<ReposViewState> =
        BehaviorSubject.createDefault(ReposViewState.initial())

    init {
        observeViewState().map { it.isLoading }
            .distinctUntilChanged()
            .filter { it }
            .flatMapCompletable { refreshDataSourse() }
            .autoDispose(this)
            .subscribe()

        reposRepository.subscribeRemoteRequestState()
            .observeOn(RxSchedulers.ui)
            .autoDispose(this)
            .subscribe { result ->
                when (result) {
                    is Result.Success -> mViewStateSubject.copyMap {
                        it.copy(
                            isLoading = false,
                            throwable = null,
                            pagedList = null,
                            nextPageData = result.data
                        )
                    }
                    is Result.Failure -> mViewStateSubject.copyMap {
                        it.copy(
                            isLoading = false,
                            throwable = result.error,
                            pagedList = null,
                            nextPageData = null
                        )
                    }
                }
            }

        reposRepository.fenchPagedListFromDb { mViewStateSubject.value!!.sort }
            .subscribeOn(RxSchedulers.io)
            .autoDispose(this)
            .subscribe { pagedList ->
                mViewStateSubject.copyMap {
                    it.copy(
                        isLoading = false,
                        throwable = null,
                        pagedList = pagedList,
                        nextPageData = null
                    )
                }
            }
    }

    fun refreshDataSourse(): Completable {
        return Completable.fromCallable { reposRepository.refreshDataSource(mViewStateSubject.value!!.sort) }
            .subscribeOn(RxSchedulers.io)
    }

    fun observeViewState(): Observable<ReposViewState> {
        return mViewStateSubject.hide().distinctUntilChanged()
    }

    fun sortChanged(sort: String) {
        if (sort != mViewStateSubject.value!!.sort) {
            mViewStateSubject.copyMap { state ->
                state.copy(
                    //会走init代码中refreshDataSourse
                    isLoading = true,
                    throwable = null,
                    pagedList = null,
                    nextPageData = null,
                    sort = sort
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        reposRepository.mAutoDisposeObserver.onNext(Unit)
        reposRepository.mAutoDisposeObserver.onComplete()
    }

    companion object {
        const val sortByCreated: String = "created"
        const val sortByUpdate: String = "updated"
        const val sortByLetter: String = "full_name"

        fun instance(fragment: Fragment, reposRepository: ReposRepository): ReposViewModel =
            ViewModelProviders.of(fragment, ReposViewModelFactory(reposRepository)).get(
                ReposViewModel::class.java
            )
    }

    class ReposViewModelFactory(private val repo: ReposRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReposViewModel(repo) as T
        }

    }
}