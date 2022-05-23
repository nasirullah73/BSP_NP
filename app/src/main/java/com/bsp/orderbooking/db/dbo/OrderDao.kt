package com.bsp.orderbooking.db.dbo

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.bsp.orderbooking.db.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(entity = OrderEntity::class, onConflict = IGNORE)
    fun insert(user: OrderEntity?)

    @Query("DELETE FROM orders WHERE _id =:Id")
    fun delete(Id: Int)

    @Query("DELETE FROM orders")
    fun deleteAll()

    @Query("SELECT * FROM orders ORDER BY _id DESC")
    suspend fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE _id = :id")
    fun getById(id: Int): OrderEntity

    @Query("SELECT  max(_id) as last_id  FROM orders")
    fun getID() : Int

    @Query("SELECT * FROM orders WHERE customerid=:id and date=:mdate")
    suspend fun getAllforCustomer(id:String,mdate:String): List<OrderEntity>

    @Update
    fun update(user: OrderEntity?)

}