package com.khb.mvvmlibrary.adapter

import android.view.View
import androidx.recyclerview.widget.BindAwareViewHolder
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 *创建时间：2019/12/11
 *编写人：kanghb
 *功能描述：
 */
abstract class AutoDisposeViewHolder(view: View) : BindAwareViewHolder(view),
    LifecycleScopeProvider<AutoDisposeViewHolder.ViewHolderEvent> {

    private val lifecycleEvents by lazy {
        BehaviorSubject.create<ViewHolderEvent>()
    }

    enum class ViewHolderEvent {
        BIND, UNBIND
    }

    override fun onBind() = lifecycleEvents.onNext(ViewHolderEvent.BIND)

    override fun onUnBind() = lifecycleEvents.onNext(ViewHolderEvent.UNBIND)

    override fun lifecycle(): Observable<ViewHolderEvent> = lifecycleEvents.hide()

    override fun correspondingEvents(): CorrespondingEventsFunction<ViewHolderEvent> =
        CORRESPONDING_EVENTS

    override fun peekLifecycle(): ViewHolderEvent? {
        return lifecycleEvents.value
    }

    companion object {
        private val CORRESPONDING_EVENTS = CorrespondingEventsFunction<ViewHolderEvent> {viewHolderEvent ->
            when(viewHolderEvent){
                ViewHolderEvent.BIND ->ViewHolderEvent.UNBIND
                else -> throw LifecycleEndedException( "Cannot use ViewHolder lifecycle after unbind.")
            }
        }
    }


}