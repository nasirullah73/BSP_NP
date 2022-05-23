package com.bsp.orderbooking.db.dbo

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.bsp.orderbooking.db.entity.ProductEntity

@Dao
interface ProductsDao {

    @Insert(entity = ProductEntity::class, onConflict = IGNORE)
    fun insert(user: ProductEntity?)

    @Query("DELETE FROM product WHERE productid =:Id")
    fun delete(Id: Int)

    @Query("DELETE FROM product")
    fun deleteAll()

    @Query("SELECT * FROM product order by productname" )
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE productid = :pid")
    fun getById(pid: Int): ProductEntity

    @Insert(entity = ProductEntity::class, onConflict = IGNORE)
    fun insertAll( users: List<ProductEntity>)

    @Query("SELECT * FROM product WHERE productname = :pid")
    fun getByName(pid: String): ProductEntity
}