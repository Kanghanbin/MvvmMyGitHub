package com.khb.mvvmmygithub.ui.main.repo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.khb.mvvmlibrary.adapter.AutoDisposeViewHolder
import com.khb.mvvmlibrary.ext.reactivex.clickThrottleFrist
import com.khb.mvvmlibrary.img.GlideApp
import com.khb.mvvmmygithub.R
import com.khb.mvvmmygithub.responseentity.Repos
import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_repos_repo.view.*

/**
 *创建时间：2019/12/16
 *编写人：kanghb
 *功能描述：
 */
class ReposPagedAdapter : PagedListAdapter<Repos, ReposViewHolder>(diffCallback) {
    private val parentSubject: PublishSubject<String> = PublishSubject.create()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repos_repo, parent, false)
        return ReposViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.binds(getItem(position)!!,parentSubject)
    }

    fun getItemClickEvent():Observable<String>{
        return parentSubject
    }


    companion object {
        private val diffCallback: DiffUtil.ItemCallback<Repos> =
            object : DiffUtil.ItemCallback<Repos>() {
                override fun areItemsTheSame(oldItem: Repos, newItem: Repos): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Repos, newItem: Repos): Boolean {
                    return oldItem == newItem
                }

            }
    }
}

class ReposViewHolder(val view: View) : AutoDisposeViewHolder(view) {
    val ivAvatar: ImageView = view.ivAvator
    val clAvator: ConstraintLayout = view.clAvator
    val tvOwnerName: TextView = view.tvOwnerName
    val civLanguage = view.civLanguage
    val tvLanguage = view.tvLanguage
    val tvRepoName = view.tvRepoName
    val tvRepoDesc = view.tvRepoDesc
    val tvStar = view.tvStar
    val tvIssue = view.tvIssue
    val tvFork = view.tvFork

    fun binds(repos: Repos, observer: Observer<String>) {
        GlideApp.with(ivAvatar.context)
            .load(repos.owner.avatar_url)
            .apply(RequestOptions().circleCrop())
            .into(ivAvatar)

        view.clickThrottleFrist()
            .map { repos.html_url }
            .autoDispose(this)
            .subscribe { url ->
                observer.onNext(url)
            }
        clAvator.clickThrottleFrist()
            .map { repos.owner.html_url }
            .autoDispose(this)
            .subscribe { url ->
                observer.onNext(url)
            }
        tvOwnerName.text = repos.owner.login
        civLanguage.setImageDrawable(getLanguageColor(repos.language))
        civLanguage.visibility = if (repos.language == null) View.GONE else View.VISIBLE
        tvLanguage.text = repos.language

        tvRepoName.text = repos.full_name
        tvRepoDesc.text = repos.description ?: "(No description, website, or topics provided.)"

        tvStar.text = repos.stargazers_count.toString()
        tvFork.text = repos.forks_count.toString()
        tvIssue.text = repos.open_issues_count.toString()

    }

    private fun getLanguageColor(language: String?): Drawable {

        if (language == null) return ColorDrawable(Color.TRANSPARENT)
        val colorProvider : (Int) -> Drawable = { resId ->
            ColorDrawable(ContextCompat.getColor(itemView.context,resId))
        }
        return colorProvider(when(language){
            "Kotlin" -> R.color.color_language_kotlin
            "Java" -> R.color.color_language_java
            "JavaScript" -> R.color.color_language_js
            "Python" -> R.color.color_language_python
            "HTML" -> R.color.color_language_html
            "CSS" -> R.color.color_language_css
            "Shell" -> R.color.color_language_shell
            "C++" -> R.color.color_language_cplus
            "C" -> R.color.color_language_c
            "ruby" -> R.color.color_language_ruby
            else -> R.color.color_language_other
        })
    }

}