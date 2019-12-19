package com.khb.mvvmmygithub.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.khb.mvvmlibrary.base.view.fragment.BaseFragment
import com.khb.mvvmlibrary.ext.reactivex.clickThrottleFrist
import com.khb.mvvmlibrary.util.RxSchedulers

import com.khb.mvvmmygithub.R
import com.khb.mvvmmygithub.http.Errors
import com.khb.mvvmmygithub.ui.main.MainActivity
import com.khb.mvvmmygithub.utils.toast
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.login_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import retrofit2.HttpException

class LoginFragment : BaseFragment() {

    override val layoutId: Int = R.layout.login_fragment
    override val kodein: Kodein = Kodein.lazy {
        extend(parentKodein)
        import(loginModule)
    }

    private val mViewModel: LoginViewModel by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binds()
    }

    private fun binds() {
        mBtnSignIn.clickThrottleFrist()
            .autoDispose(scropeProvider)
            .subscribe { mViewModel.login(tvUsername.text.toString(), tvPassword.text.toString()) }
        mViewModel.observeViewState()
            .observeOn(RxSchedulers.ui)
            .autoDispose(scropeProvider)
            .subscribe  (this::onNewState)


    }

    private fun onNewState(state: LoginViewState) {
        if (state.throwable != null) {
            when (state.throwable) {
                is Errors.EmptyInputError -> "username or password can't be null."
                is HttpException ->
                    when (state.throwable.code()) {
                        401 -> "username or password failure."
                        else -> "network failure"
                    }
                else -> "network failure"
            }.also {
                toast { it }
            }
        }

        mProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        if (state.autoLoginEvent != null && state.autoLoginEvent.autoLogin && state.useAutoLogin){
            tvUsername.setText(state.autoLoginEvent.username, TextView.BufferType.EDITABLE)
            tvPassword.setText(state.autoLoginEvent.password, TextView.BufferType.EDITABLE)
            mViewModel.onAutoLoginEventUsed()
            mViewModel.login(state.autoLoginEvent.username,state.autoLoginEvent.password)
        }

        if(state.loginInfo != null){
            MainActivity.launch(activity!!)
        }
    }

}
