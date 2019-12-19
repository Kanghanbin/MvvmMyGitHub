package com.khb.mvvmmygithub.ui.main.mine

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.khb.mvvmlibrary.base.viewmodel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 *创建时间：2019/12/17
 *编写人：kanghb
 *功能描述：
 */
class MineViewModel(mineRepository: MineRepository) : BaseViewModel() {

    private val behaviorSubject: BehaviorSubject<MineViewState> =
        BehaviorSubject.createDefault(MineViewState.intital())

    fun observeViewState(): Observable<MineViewState> {
        return behaviorSubject.hide().distinctUntilChanged()
    }

    companion object {
        fun instance(frament: Fragment, mineRepository: MineRepository): MineViewModel {

            return ViewModelProviders.of(frament, MineViewModelFactory(mineRepository))
                .get(MineViewModel::class.java)
        }

    }

    class MineViewModelFactory(val mineRepository: MineRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MineViewModel(mineRepository) as T
        }

    }
}