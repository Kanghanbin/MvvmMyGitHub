package com.khb.mvvmmygithub.ui.main.mine

import com.khb.mvvmlibrary.base.repository.BaseRemoteDataSourse
import com.khb.mvvmlibrary.base.repository.IRemoteDataSourse
import com.khb.mvvmmygithub.http.ServiceManager
import com.khb.mvvmmygithub.ui.main.repo.LocalReposDataSourse

/**
 *创建时间：2019/12/17
 *编写人：kanghb
 *功能描述：
 */

class MineRepository(mineRemoteDataSourse: MineRemoteDataSourse) :
    BaseRemoteDataSourse<MineRemoteDataSourse>(mineRemoteDataSourse) {

}

class MineRemoteDataSourse(serviceManager: ServiceManager) : IRemoteDataSourse {

}