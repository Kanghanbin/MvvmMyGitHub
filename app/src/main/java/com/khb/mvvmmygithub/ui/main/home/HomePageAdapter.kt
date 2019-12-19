package com.khb.mvvmmygithub.ui.main.home

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.khb.mvvmlibrary.adapter.AutoDisposeViewHolder
import com.khb.mvvmlibrary.img.GlideApp
import com.khb.mvvmmygithub.R
import com.khb.mvvmmygithub.responseentity.ReceivedEvent
import com.khb.mvvmmygithub.responseentity.Type
import com.khb.mvvmmygithub.utils.TimeConverter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_home_received_event.*
import kotlinx.android.synthetic.main.item_home_received_event.view.*
import org.kodein.di.generic.contextFinder

/**
 *创建时间：2019/12/11
 *编写人：kanghb
 *功能描述：
 */
class HomePageAdapter : PagedListAdapter<ReceivedEvent, HomeViewHolder>(diffCallback) {


    private val parentSubject: PublishSubject<String> = PublishSubject.create()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_received_event, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindData(getItem(position)!!, parentSubject)
    }

    fun getItemClickEvent(): Observable<String>{
        return parentSubject
    }

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<ReceivedEvent> = object :
            DiffUtil.ItemCallback<ReceivedEvent>() {
            override fun areContentsTheSame(
                oldItem: ReceivedEvent,
                newItem: ReceivedEvent
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ReceivedEvent, newItem: ReceivedEvent): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }
}

class HomeViewHolder(view: View) : AutoDisposeViewHolder(view) {

    private val tvEventContent = view.tvEventContent
    private val tvEventTime = view.tvEventTime
    private val ivAvator = view.ivAvator
    private val ivEventType = view.ivEventType

    fun bindData(data: ReceivedEvent, observer: Observer<String>) {
        GlideApp.with(ivAvator.context)
            .load(data.actor.avatar_url)
            .apply(RequestOptions().circleCrop())
            .into(ivAvator)
        tvEventContent.text = getTitle(data, observer)
        tvEventTime.text = TimeConverter.transTimeAgo(data.created_at)
        ivEventType.setImageDrawable(
            when (data.type) {
                Type.WatchEvent ->
                    ContextCompat.getDrawable(ivEventType.context, R.mipmap.ic_star_yellow_light)
                Type.ForkEvent, Type.CreateEvent, Type.PushEvent ->
                    ContextCompat.getDrawable(ivEventType.context, R.mipmap.ic_fork_green_light)
                else -> null
            }
        )

    }

    private fun getTitle(data: ReceivedEvent, observer: Observer<String>): CharSequence? {
        val actor = data.actor.display_login
        val action = when (data.type) {
            Type.WatchEvent -> "starred"
            Type.ForkEvent -> "forked"
            Type.CreateEvent -> "created"
            Type.PushEvent -> "pushed"
            else -> data.type.name
        }
        val repo = data.repo.name
        //点击span
        val actorSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                observer.onNext(data.actor.url)
            }
        }
        val repoSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                observer.onNext(data.repo.url)
            }
        }
        //加速span
        val styleSpan = StyleSpan(Typeface.BOLD)
        val styleSpan1 = StyleSpan(Typeface.BOLD)
        //设置movementmethod使span点击操作可以执行
        tvEventContent.movementMethod = LinkMovementMethod.getInstance()
        return SpannableStringBuilder().apply {
            append("$actor $action $repo")
            setSpan(actorSpan, 0, actor.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(styleSpan, 0, actor.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(
                repoSpan,
                actor.length + action.length + 2,
                actor.length + action.length + repo.length + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                styleSpan1,
                actor.length + action.length + 2,
                actor.length + action.length + repo.length + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

}