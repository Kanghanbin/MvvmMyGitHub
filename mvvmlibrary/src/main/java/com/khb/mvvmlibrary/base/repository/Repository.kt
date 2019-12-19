package com.khb.mvvmlibrary.base.repository

/**
 *创建时间：2019/11/5
 *编写人：kanghb
 *功能描述：
 */

open class BaseRespositoryBoth<T : ILocalDataSourse, R : IRemoteDataSourse>(
    val localDataSourse: T,
    val remoteDataSourse: R
) : IRepository

open class BaseLocalDataSourse<T : ILocalDataSourse>(
    val localDataSourse: T
) : IRepository

open class BaseRemoteDataSourse<T : IRemoteDataSourse>(
    val remoteDataSourse: T
) : IRepository

open class BaseDataSourseNothing() : IRepository