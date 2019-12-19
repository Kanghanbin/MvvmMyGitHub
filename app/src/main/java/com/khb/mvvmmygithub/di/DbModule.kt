package com.khb.mvvmmygithub.di

import androidx.room.Room
import com.khb.mvvmmygithub.base.BaseApplication
import com.khb.mvvmmygithub.db.UserDataBase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 *创建时间：2019/12/5
 *编写人：kanghb
 *功能描述：
 */
private const val DB_MODULE_TAG = "DBModule"

val dbModule = Kodein.Module(DB_MODULE_TAG) {
    bind<UserDataBase>() with singleton {
        Room.databaseBuilder(BaseApplication.INSTANCE, UserDataBase::class.java, "user")
            //迁移升级时数据被清除，谨慎使用,安全升级迁移数据库，还是使用addMigrations
            .fallbackToDestructiveMigration()
//            .addMigrations()
            .build()
    }
}