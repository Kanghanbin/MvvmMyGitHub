package com.khb.mvvmmygithub.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khb.mvvmmygithub.responseentity.ReceivedEvent
import com.khb.mvvmmygithub.responseentity.Repos

/**
 *创建时间：2019/12/2
 *编写人：kanghb
 *功能描述：
 */
@Database(
    entities = [ReceivedEvent::class, Repos::class],
    version = 3
)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userReceivedEventDao(): ReceivedEventDao

    abstract fun userReposDao(): ReposDao
}