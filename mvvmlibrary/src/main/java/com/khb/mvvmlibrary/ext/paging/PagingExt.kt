package com.khb.mvvmlibrary.ext.paging

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.khb.mvvmlibrary.util.RxSchedulers
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler

/**
 *创建时间：2019/12/9
 *编写人：kanghb
 *功能描述：
 */
private const val PAGING_DEFAULT_PAGE_SIZE = 15
private const val PAGING_DEFAULT_PREFETCH_DISTANCE = 15
private const val PAGING_DEFAULT_INITIAL_LOAD_SIZE_HINT = 30

fun <Key, Value> DataSource.Factory<Key, Value>.toRxPagingList(
    config: PagedList.Config? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fenchScheduler: Scheduler = RxSchedulers.io
): Flowable<PagedList<Value>> {
    return RxPagedListBuilder<Key, Value>(
        this, config ?: PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGING_DEFAULT_INITIAL_LOAD_SIZE_HINT)// 初始化加载的数量
            .setPageSize(PAGING_DEFAULT_PAGE_SIZE)// 分页加载的数量
            .setPrefetchDistance(PAGING_DEFAULT_PREFETCH_DISTANCE)// 预加载距离
            .setEnablePlaceholders(false)// 是否启用占位符
            .build()
    )
        .setBoundaryCallback(boundaryCallback)
        .setFetchScheduler(fenchScheduler)
        .buildFlowable(BackpressureStrategy.LATEST)
}