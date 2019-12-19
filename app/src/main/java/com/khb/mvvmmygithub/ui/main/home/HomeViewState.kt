package com.khb.mvvmmygithub.ui.main.home

import androidx.paging.PagedList
import com.khb.mvvmmygithub.responseentity.ReceivedEvent

/**
 *创建时间：2019/12/9
 *编写人：kanghb
 *功能描述：
 */
data class HomeViewState(
    val isLoading: Boolean,
    val throwable: Throwable?,
    val pagedList: PagedList<ReceivedEvent>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as HomeViewState
        if (isLoading != other.isLoading) return false
        if (throwable != other.throwable) return false
        if (pagedList != other.pagedList) return false
        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + (throwable?.hashCode() ?: 0)
        result = 31 * result + (pagedList?.hashCode() ?: 0)
        return super.hashCode()

    }

    companion object {
        fun initial(): HomeViewState {
            return HomeViewState(
                isLoading = false,
                throwable = null,
                pagedList = null
            )
        }
    }
}