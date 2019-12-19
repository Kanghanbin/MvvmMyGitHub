package com.khb.mvvmmygithub.ui.main.home

import android.annotation.SuppressLint
import androidx.paging.PagedList
import com.khb.mvvmlibrary.base.repository.BaseRespositoryBoth
import com.khb.mvvmlibrary.base.repository.ILocalDataSourse
import com.khb.mvvmlibrary.base.repository.IRemoteDataSourse
import com.khb.mvvmlibrary.ext.paging.toRxPagingList
import com.khb.mvvmlibrary.util.RxSchedulers
import com.khb.mvvmmygithub.base.Result
import com.khb.mvvmmygithub.common.PAGING_REMOTE_PAGE_SIZE
import com.khb.mvvmmygithub.db.UserDataBase
import com.khb.mvvmmygithub.http.GlobalErrorTransformer
import com.khb.mvvmmygithub.http.ServiceManager
import com.khb.mvvmmygithub.manager.UserManager
import com.khb.mvvmmygithub.responseentity.ReceivedEvent
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.AsyncProcessor
import io.reactivex.processors.PublishProcessor

/**
 *创建时间：2019/12/9
 *编写人：kanghb
 *功能描述：
 */
@SuppressLint("CheckResult")
class HomeRepository(
    localDataSourse: HomeLocalDataSourse,
    remoteDataSourse: HomeRemoteDataSourse,
    val autoDisposeObserver: AsyncProcessor<Unit> = AsyncProcessor.create()
) : BaseRespositoryBoth<HomeLocalDataSourse, HomeRemoteDataSourse>(
    localDataSourse,
    remoteDataSourse
) {

    private val remoteRequestStateProcessor: PublishProcessor<Result<List<ReceivedEvent>>> =
        PublishProcessor.create()
    //供HomeViewModel调用
    fun subscribeRemoteRequestState(): Flowable<Result<List<ReceivedEvent>>> {
        return remoteRequestStateProcessor.hide()
    }

    /**
     * 从数据库获取数据
     */
    fun initPagedListFromDb(): Flowable<PagedList<ReceivedEvent>> {
        return localDataSourse.fenchPagedListFromDb(
            boundaryCallback = object : PagedList.BoundaryCallback<ReceivedEvent>() {
                //从PageList的数据源(Room数据库)返回零条结果时调用。
                override fun onZeroItemsLoaded() {
                    fenchEventByPage(1)
                        .takeUntil(autoDisposeObserver)
                        .subscribe { remoteRequestStateProcessor.onNext(it) }

                }

                //加载PageList后面的数据项时被调用
                override fun onItemAtEndLoaded(itemAtEnd: ReceivedEvent) {
                    val currentPageIndex = (itemAtEnd.indexInResponse / 30) + 1
                    val nextPageIndex = currentPageIndex + 1
                    fenchEventByPage(nextPageIndex)
                        .takeUntil(autoDisposeObserver)
                        .subscribe { remoteRequestStateProcessor.onNext(it) }
                }

            })
    }


    fun refreshPagedList() {
        fenchEventByPage(1)
            .takeUntil(autoDisposeObserver)
            .subscribe { remoteRequestStateProcessor.onNext(it) }
    }

    private fun fenchEventByPage(
        pageIndex: Int,
        remoteRequestPerPage: Int = PAGING_REMOTE_PAGE_SIZE
    ): Flowable<Result<List<ReceivedEvent>>> {
        val username: String = UserManager.INSTANCE.login
        return when (pageIndex) {
            1 -> remoteDataSourse.fenchEventByPage(username, pageIndex, remoteRequestPerPage)
                .flatMap { result ->
                    when (result) {
                        is Result.Success ->
                            localDataSourse.clearOldAndInsertNewData(result.data)
                                .andThen(Flowable.just(result))
                        else ->
                            Flowable.just(result)

                    }
                }
            else -> remoteDataSourse.fenchEventByPage(username, pageIndex, remoteRequestPerPage)
                .flatMap { result ->
                    when (result) {
                        is Result.Success ->
                            localDataSourse.insertNewData(result.data)
                                .andThen(Flowable.just(result))
                        else ->
                            Flowable.just(result)
                    }
                }
        }

    }
}

class HomeRemoteDataSourse(private val serviceManager: ServiceManager) : IRemoteDataSourse {
    fun fenchEventByPage(
        username: String,
        pageIndex: Int,
        perPage: Int
    ): Flowable<Result<List<ReceivedEvent>>> {
        return when (pageIndex) {
            1 -> fenchEventsByPageInternal(username, pageIndex, perPage)
                .map { Result.success(it) }
                .onErrorReturn { Result.failue(it) }
                .startWith(Result.loading())
                .startWith(Result.idle())
            else -> fenchEventsByPageInternal(username, pageIndex, perPage)
                .map { Result.success(it) }
                .onErrorReturn { Result.failue(it) }
        }
    }

    private fun fenchEventsByPageInternal(
        username: String,
        pageIndex: Int,
        perPage: Int
    ): Flowable<List<ReceivedEvent>> {
        return serviceManager.userService
            .queryReceivedEvents(username, pageIndex, perPage)
            .observeOn(RxSchedulers.ui)
            .compose(GlobalErrorTransformer())
            .observeOn(RxSchedulers.io)
            .subscribeOn(RxSchedulers.io)

    }

}

class HomeLocalDataSourse(private val db: UserDataBase) : ILocalDataSourse {

    //从数据库中获取数据
    fun fenchPagedListFromDb(boundaryCallback: PagedList.BoundaryCallback<ReceivedEvent>): Flowable<PagedList<ReceivedEvent>> {

        return db.userReceivedEventDao().queryEvents()
            .toRxPagingList(
                boundaryCallback = boundaryCallback,
                fenchScheduler = RxSchedulers.io
            )
    }

    fun clearOldAndInsertNewData(newPage: List<ReceivedEvent>): Completable {
        return Completable.fromAction {
            db.runInTransaction {
                db.userReceivedEventDao().clearReceivedEvents()
                insertDataInternal(newPage)
            }
        }
    }

    fun insertNewData(newPage: List<ReceivedEvent>): Completable {
        return Completable.fromAction {
            db.runInTransaction {
                insertDataInternal(newPage)
            }
        }.subscribeOn(RxSchedulers.io)
    }

    private fun insertDataInternal(newPage: List<ReceivedEvent>) {
        val start = db.userReceivedEventDao().getNextIndexInResponseEvents()
        //序列号依次递增
        val items = newPage.mapIndexed { index, receivedEvent ->
            receivedEvent.indexInResponse = start + index
            receivedEvent
        }
        db.userReceivedEventDao().insert(items)

    }
}