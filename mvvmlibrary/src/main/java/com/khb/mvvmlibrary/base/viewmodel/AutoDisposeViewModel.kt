package com.khb.mvvmlibrary.base.viewmodel

import androidx.lifecycle.ViewModel
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 *创建时间：2019/11/6
 *编写人：kanghb
 *功能描述：
 */
open class AutoDisposeViewModel : ViewModel(), IViewModel,
    LifecycleScopeProvider<AutoDisposeViewModel.ViewModelEvent> {


    private val lifecycleEvents = BehaviorSubject.createDefault(ViewModelEvent.CREATED)

    override fun lifecycle(): Observable<ViewModelEvent> {
        return lifecycleEvents.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<ViewModelEvent> {
        return CORRESPODING_EVENTS
    }

    override fun peekLifecycle(): ViewModelEvent? {
        return lifecycleEvents.value
    }

    override fun onCleared() {
        lifecycleEvents.onNext(ViewModelEvent.CLEARED)
        super.onCleared()
    }

    enum class ViewModelEvent {
        CREATED, CLEARED
    }


    companion object {

        private val CORRESPODING_EVENTS = CorrespondingEventsFunction<ViewModelEvent> { event ->
            when (event) {
                ViewModelEvent.CREATED -> ViewModelEvent.CLEARED
                else -> throw LifecycleEndedException("Cannot bind to ViewModel lifecycle after onCleared.")
            }

        }
    }
}