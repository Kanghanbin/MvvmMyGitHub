package com.khb.mvvmmygithub.ui.main.repo

import androidx.paging.PagedList
import com.khb.mvvmmygithub.responseentity.Repos

/**
 *创建时间：2019/12/13
 *编写人：kanghb
 *功能描述：
 */
data class ReposViewState(
    val isLoading: Boolean,
    val throwable: Throwable?,
    val pagedList: PagedList<Repos>?,
    val nextPageData: List<Repos>?,
    val sort: String

) {
    companion object{
        fun initial():ReposViewState {
            return ReposViewState(
                isLoading = false,
                throwable = null,
                pagedList = null,
                nextPageData = null,
                sort = ReposViewModel.sortByUpdate
            )
        }
    }
}