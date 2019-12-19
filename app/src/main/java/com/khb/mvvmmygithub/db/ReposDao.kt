package com.khb.mvvmmygithub.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khb.mvvmmygithub.responseentity.Repos

/**
 *创建时间：2019/12/3
 *编写人：kanghb
 *功能描述：
 */
@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Repos>)

    @Query("select * from user_repos order by indexInSortResponse asc")
    fun queryRepos(): DataSource.Factory<Int, Repos>

    @Query("delete from user_repos")
    fun clearRepos()

    @Query("select max(indexInSortResponse) + 1 from user_repos")
    fun getNextIndexResponse(): Int
}