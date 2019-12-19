package com.khb.mvvmmygithub.ui.main.repo

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
import com.khb.mvvmmygithub.responseentity.Repos
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.AsyncProcessor
import io.reactivex.processors.PublishProcessor

/**
 *创建时间：2019/12/13
 *编写人：kanghb
 *功能描述：
 */
@SuppressLint("CheckResult")
class ReposRepository(
    local: LocalReposDataSourse,
    remote: RemoteReposDataSourse,
    //不管在什么位置订阅，都只接接收到最后一条数据
    val mAutoDisposeObserver: AsyncProcessor<Unit> = AsyncProcessor.create()
) : BaseRespositoryBoth<LocalReposDataSourse, RemoteReposDataSourse>(local, remote) {
    //PublicSubject：接收到订阅之后的所有数据。
    private val mRemoteRequestStateProcessor: PublishProcessor<Result<List<Repos>>> =
        PublishProcessor.create()

    fun subscribeRemoteRequestState(): Flowable<Result<List<Repos>>> =
        mRemoteRequestStateProcessor.hide()

    fun refreshDataSource(sort: String) {
        fenchRepoByPage(sort, 1).takeUntil(mAutoDisposeObserver)
            .subscribe { mRemoteRequestStateProcessor.onNext(it) }
    }

    fun fenchRepoByPage(
        sort: String,
        pageIndex: Int,
        remoteRequestPerPage: Int = PAGING_REMOTE_PAGE_SIZE
    ): Flowable<Result<List<Repos>>> {
        val userName: String = UserManager.INSTANCE.login
        return when (pageIndex == 1) {
            true -> remoteDataSourse.fenchReposFromRemote(
                userName,
                pageIndex,
                remoteRequestPerPage,
                sort
            ).flatMap { result ->
                when (result is Result.Success) {
                    true -> localDataSourse.clearOldAndInsertNewData(result.data).andThen(
                        Flowable.just(result)
                    )
                    false -> Flowable.just(result)
                }
            }
            false -> remoteDataSourse.fenchReposFromRemote(
                userName,
                pageIndex,
                remoteRequestPerPage,
                sort
            ).flatMap { result ->
                when (result is Result.Success) {
                    true -> localDataSourse.insertNewPageData(result.data).andThen(
                        Flowable.just(result)
                    )
                    false -> Flowable.just(result)
                }
            }
        }
    }

    fun fenchPagedListFromDb(sort: () -> String): Flowable<PagedList<Repos>> {
        return localDataSourse.fenchPagedListFromDb(
            boundraryCallback = object : PagedList.BoundaryCallback<Repos>() {
                override fun onZeroItemsLoaded() {
                    refreshDataSource(sort())
                }

                override fun onItemAtEndLoaded(itemAtEnd: Repos) {
                    val currentPageIndex = (itemAtEnd.indexInSortResponse / 30) + 1
                    val nextPageIndex = currentPageIndex + 1
                    fenchRepoByPage(sort(), nextPageIndex).takeUntil(mAutoDisposeObserver)
                        .subscribe { mRemoteRequestStateProcessor.onNext(it) }
                }

            }
        )
    }
}

class RemoteReposDataSourse(private val serviceManager: ServiceManager) : IRemoteDataSourse {
    fun fenchReposFromRemote(
        userName: String,
        pageIndex: Int,
        perPage: Int,
        sort: String
    ): Flowable<Result<List<Repos>>> {
        return fenchReposInternal(userName, pageIndex, perPage, sort)
            .map { Result.success(it) }
            .onErrorReturn { Result.failue(it) }

    }

    private fun fenchReposInternal(
        userName: String,
        pageIndex: Int,
        perPage: Int,
        sort: String
    ): Flowable<List<Repos>> {

        return serviceManager.userService.queryRepos(userName, pageIndex, perPage, sort)
            .observeOn(RxSchedulers.ui)
            .compose(GlobalErrorTransformer())
            .observeOn(RxSchedulers.ui)
            .subscribeOn(RxSchedulers.io)
    }

}


class LocalReposDataSourse(private val db: UserDataBase) : ILocalDataSourse {

    fun fenchPagedListFromDb(boundraryCallback: PagedList.BoundaryCallback<Repos>): Flowable<PagedList<Repos>> {
        return db.userReposDao().queryRepos()
            .toRxPagingList(boundaryCallback = boundraryCallback, fenchScheduler = RxSchedulers.io)
    }

    fun clearOldAndInsertNewData(newPage: List<Repos>): Completable {
        return Completable.fromAction {
            db.runInTransaction {
                db.userReposDao().clearRepos()
                insertDataInternal(newPage)
            }
        }.subscribeOn(RxSchedulers.io)
    }

    fun insertNewPageData(newPage: List<Repos>): Completable {
        return Completable.fromAction {
            db.runInTransaction { insertDataInternal(newPage) }
        }.subscribeOn(RxSchedulers.io)
    }

    private fun insertDataInternal(newPage: List<Repos>) {
        val start = db.userReposDao().getNextIndexResponse()
        val items = newPage.mapIndexed { index, repos ->
            repos.indexInSortResponse = start + index
            repos
        }
        db.userReposDao().insert(items)
    }
}