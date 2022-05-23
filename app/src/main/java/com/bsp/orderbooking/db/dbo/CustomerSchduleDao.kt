package com.bsp.orderbooking.db.dbo

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.bsp.orderbooking.db.entity.CustomerSchedule

@Dao
interface CustomerSchduleDao {

    @Insert(entity = CustomerSchedule::class, onConflict = IGNORE)
    fun insert(user: CustomerSchedule?)

    @Query("DELETE FROM cussch WHERE _id =:Id")
    fun delete(Id: Int)

    @Query("DELETE FROM cussch")
    fun deleteAll()

    @Query("SELECT * FROM cussch")
    suspend fun getAll(): List<CustomerSchedule>

    @Query("SELECT * FROM cussch WHERE REGIONID = :id")
    suspend fun getAllByRegId(id: Int?): List<CustomerSchedule>

    @Query("SELECT * FROM cussch WHERE _id = :id")
    fun getById(id: Int): CustomerSchedule

    @Insert(entity = CustomerSchedule::class, onConflict = IGNORE)
    fun insertAll( users: List<CustomerSchedule>)

}