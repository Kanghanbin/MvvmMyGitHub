package com.khb.mvvmmygithub.ui.login

import com.khb.mvvmlibrary.base.repository.BaseRemoteDataSourse
import com.khb.mvvmlibrary.base.repository.BaseRespositoryBoth
import com.khb.mvvmlibrary.base.repository.ILocalDataSourse
import com.khb.mvvmlibrary.base.repository.IRemoteDataSourse
import com.khb.mvvmlibrary.util.RxSchedulers
import com.khb.mvvmmygithub.db.UserDataBase
import com.khb.mvvmmygithub.http.ServiceManager
import com.khb.mvvmmygithub.manager.UserManager
import com.khb.mvvmmygithub.repository.UserInfoRepository
import com.khb.mvvmmygithub.requestbean.UserTokenRequest
import com.khb.mvvmmygithub.responseentity.UserInfo
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 *创建时间：2019/11/11
 *编写人：kanghb
 *功能描述：关于登录的Repository
 */
class LoginRepository(
    loginLocalDataSourse: LoginLocalDataSourse,
    remoteDataSourse: LoginRemoteDataSourse
) : BaseRespositoryBoth<LoginLocalDataSourse, LoginRemoteDataSourse>(
    loginLocalDataSourse, remoteDataSourse
) {
    fun login(username: String, password: String): Flowable<UserInfo> {
        return localDataSourse.savePrefsUser(username, password)
            .andThen(remoteDataSourse.login())
            .doOnNext { UserManager.INSTANCE = it }
            .doOnError { localDataSourse.clearPrefsUser() }
    }

    fun fenchAutoLogin(): Flowable<AutoLoginEvent> {
        return localDataSourse.fenchAutoLogin()
    }
}


class LoginRemoteDataSourse(
    private val serviceManager: ServiceManager
) : IRemoteDataSourse {
    fun login(): Flowable<UserInfo> {
//       这种授权过时了
//        val authObservable = serviceManager.userService.authorizations(UserTokenRequest.generate())
//        val ownerInfoObservable = serviceManager.userService.fenchUserOwner()
//        return authObservable.flatMap { ownerInfoObservable }
//            .subscribeOn(RxSchedulers.io)

//        直接读取token
        return serviceManager.userService.fenchUserOwner()
            .subscribeOn(RxSchedulers.io)

    }
}

class LoginLocalDataSourse(
    private val db: UserDataBase,
    private val userInfoRepository: UserInfoRepository
) : ILocalDataSourse {

    fun savePrefsUser(username: String, password: String): Completable {
        return Completable.fromAction {
            userInfoRepository.username = username
            userInfoRepository.password = password
        }
    }

    fun clearPrefsUser(): Completable {
        return Completable.fromAction {
            userInfoRepository.username = ""
            userInfoRepository.password = ""
        }
    }

    fun fenchAutoLogin(): Flowable<AutoLoginEvent> {
        val username = userInfoRepository.username
        val password = userInfoRepository.password
        val isAutoLogin = userInfoRepository.isAutoLogin
        return Flowable.just(
            when (username.isNotEmpty() && password.isNotEmpty() && isAutoLogin) {
                true -> AutoLoginEvent(username, password, isAutoLogin)
                false -> AutoLoginEvent("", "", false)
            }
        )
    }

}

data class AutoLoginEvent(
    val username: String,
    val password: String,
    val autoLogin: Boolean
)