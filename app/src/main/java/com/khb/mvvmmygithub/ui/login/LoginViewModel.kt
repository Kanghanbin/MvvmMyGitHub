package com.khb.mvvmmygithub.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khb.mvvmlibrary.base.viewmodel.BaseViewModel
import com.khb.mvvmlibrary.ext.reactivex.copyMap
import com.khb.mvvmmygithub.base.Result
import com.khb.mvvmmygithub.http.Errors
import com.khb.mvvmmygithub.http.GlobalErrorTransformer
import com.uber.autodispose.autoDispose

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class LoginViewModel(
    private val repo: LoginRepository
) : BaseViewModel() {
    private val mViewStateSubject: BehaviorSubject<LoginViewState> = BehaviorSubject.createDefault(
        LoginViewState.initial()
    )

    init {
        //一进来就获取自动登录一下
        repo.fenchAutoLogin().singleOrError()
            .onErrorReturn { AutoLoginEvent("", "", false) }
            .autoDispose(this)
            .subscribe { event ->
                mViewStateSubject.copyMap {
                    it.copy(
                        isLoading = false,
                        throwable = null,
                        autoLoginEvent = event,
                        loginInfo = null
                    )
                }
            }
    }

    fun observeViewState(): Observable<LoginViewState> {
        //隐藏自己的观察者身份，纯粹的作为被观察者，去除连续重复
        return mViewStateSubject.hide().distinctUntilChanged()
    }

    fun onAutoLoginEventUsed() {
        mViewStateSubject.copyMap { state ->
            state.copy(
                isLoading = false,
                throwable = null,
                useAutoLogin = false,
                loginInfo = null
            )
        }
    }

    fun login(username: String?, password: String?) {
        when (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            true -> mViewStateSubject.copyMap {
                it.copy(
                    isLoading = false,
                    throwable = Errors.EmptyInputError,
                    loginInfo = null,
                    autoLoginEvent = null
                )
            }
            false -> repo.login(username, password)
                .compose(GlobalErrorTransformer())
                .map { Result.success(it) }
                .startWith(Result.loading())
                .startWith(Result.idle())
                .onErrorReturn { Result.failue(it) }
                .autoDispose(this)
                .subscribe { state ->
                    when (state) {
                        is Result.Loading -> mViewStateSubject.copyMap {
                            it.copy(
                                isLoading = true,
                                throwable = null,
                                loginInfo = null
                            )
                        }
                        is Result.Idle -> mViewStateSubject.copyMap {
                            it.copy(
                                isLoading = false,
                                throwable = null,
                                loginInfo = null
                            )
                        }
                        is Result.Failure -> mViewStateSubject.copyMap {
                            it.copy(
                                isLoading = false,
                                throwable = state.error,
                                loginInfo = null
                            )
                        }
                        is Result.Success -> mViewStateSubject.copyMap {
                            it.copy(
                                isLoading = false,
                                throwable = null,
                                loginInfo = state.data
                            )
                        }
                    }
                }
        }
    }

}

class LoginViewModelFactory(
    val repository: LoginRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }

}
