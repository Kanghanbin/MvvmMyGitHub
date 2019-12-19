package com.khb.mvvmmygithub.ui.main.mine


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.khb.mvvmlibrary.base.view.fragment.BaseFragment
import com.khb.mvvmlibrary.img.GlideApp
import com.khb.mvvmlibrary.util.RxSchedulers

import com.khb.mvvmmygithub.R
import com.khb.mvvmmygithub.ui.main.mainModule
import com.khb.mvvmmygithub.utils.toast
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_mine.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import kotlin.math.min


class MineFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_mine
    override val kodein: Kodein = Kodein.lazy {
        extend(parentKodein)
        import(mineModule)
    }

    private val viewModel: MineViewModel by instance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binds()
    }

    private fun binds() {
        viewModel.observeViewState()
            .observeOn(RxSchedulers.ui)
            .autoDispose(scropeProvider)
            .subscribe(::onNewState)
    }

    private fun onNewState(state:MineViewState){

        if(state.throwable != null){
            toast { "network failure" }
        }
        if(state.userInfo != null){
            GlideApp.with(context!!)
                .load(state.userInfo.avatarUrl)
                .apply(RequestOptions().circleCrop())
                .into(ivAvator)

            tvNickName.text = state.userInfo.name
            tvBio.text = state.userInfo.bio
            tvLocation.text = state.userInfo.location
        }


    }

}
