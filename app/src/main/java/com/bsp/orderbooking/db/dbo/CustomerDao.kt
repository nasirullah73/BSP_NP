package com.bsp.orderbooking.db.dbo

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.bsp.orderbooking.db.entity.Customer

@Dao
interface CustomerDao {

    @Insert(entity = Customer::class, onConflict = IGNORE)
    fun insert(customer: Customer?)

    @Query("DELETE FROM customers WHERE customerid =:Id")
    fun delete(Id: Int)

    @Query("DELETE FROM customers")
    fun deleteAll()

    @Query("SELECT * FROM customers order by orgname")
    suspend fun getAll(): List<Customer>

    @Query("SELECT * FROM customers WHERE customerid = :id")
    fun getById(id: Int?): Customer?

    @Insert(entity = Customer::class, onConflict = IGNORE)
    fun insertAll(objects : List<Customer>)


}