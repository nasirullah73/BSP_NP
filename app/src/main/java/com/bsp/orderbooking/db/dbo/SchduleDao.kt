package com.bsp.orderbooking.db.dbo

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.bsp.orderbooking.db.entity.Schedule

@Dao
interface SchduleDao {

    @Insert(entity = Schedule::class,onConflict = IGNORE)
    fun insert(user: Schedule?)

    @Query("DELETE FROM schdule WHERE _id =:Id")
    fun delete(Id: Int)

    @Query("DELETE FROM schdule")
    fun deleteAll()

    @Query("SELECT * FROM schdule")
    suspend fun getAll(): List<Schedule>

    @Query("SELECT * FROM schdule WHERE employeeid = :id")
    suspend fun getAllByEmpId(id: Int): List<Schedule>

    @Query("SELECT * FROM schdule WHERE _id = :id")
    fun getById(id: Int): Schedule

    @Insert(entity = Schedule::class,onConflict = IGNORE)
    fun insertAll( users: List<Schedule>)

}