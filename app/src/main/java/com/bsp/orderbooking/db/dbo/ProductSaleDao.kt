package com.bsp.orderbooking.db.dbo

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.ProductSale

@Dao
interface ProductSaleDao {

    @Insert(entity = ProductSale::class, onConflict = IGNORE)
    fun insert(user: ProductSale?)

    @Query("DELETE FROM product_sale WHERE _id =:Id")
    fun delete(Id: Int)

    @Query("DELETE FROM product_sale")
    fun deleteAll()

    @Query("SELECT * FROM product_sale WHERE status = 'P'")
    suspend fun getAll(): List<ProductSale>

    @Query("SELECT * FROM product_sale WHERE orderid = :oid")
    suspend fun getAllById(oid: String): List<ProductSale>

    @Query("SELECT * FROM product_sale WHERE _id = :id")
    fun getById(id: Int): ProductSale

    @Update
    fun update(user: ProductSale?)

}