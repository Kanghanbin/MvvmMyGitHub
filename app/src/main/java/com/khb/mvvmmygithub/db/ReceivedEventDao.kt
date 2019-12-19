package com.khb.mvvmmygithub.db

import androidx.paging.DataSource
import androidx.room.*
import com.khb.mvvmmygithub.responseentity.ReceivedEvent

/**
 *创建时间：2019/12/2
 *编写人：kanghb
 *功能描述：
 */
@Dao
interface ReceivedEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<ReceivedEvent>)

    @Query("select * from received_event order by indexInResponse asc")
    fun queryEvents(): DataSource.Factory<Int, ReceivedEvent>

    @Query("select max(indexInResponse) + 1 from received_event")
    fun getNextIndexInResponseEvents(): Int

    @Query("delete from received_event")
    fun clearReceivedEvents()

}