package com.bsp.orderbooking.db.database

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bsp.orderbooking.db.dbo.*
import com.bsp.orderbooking.db.entity.*

@Database(entities = [
    Customer::class,
    ProductEntity::class,
    Schedule::class,
    CustomerSchedule::class,
    ProductSale::class,
    OrderEntity::class
    ], version = 4,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductsDao
    abstract fun schduleDao(): SchduleDao
    abstract fun customerSchduleDao(): CustomerSchduleDao
    abstract fun productSaleDao(): ProductSaleDao
    abstract fun orderDao(): OrderDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val NUMBER_OF_THREADS = 8
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "BspAppDatabase2")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    val cDeo: CustomerDao = INSTANCE!!.customerDao()
                    val pDeo: ProductsDao = INSTANCE!!.productDao()
                    val sDeo: SchduleDao = INSTANCE!!.schduleDao()
                    val csDeo: CustomerSchduleDao = INSTANCE!!.customerSchduleDao()
                    val psDeo: ProductSaleDao = INSTANCE!!.productSaleDao()
                    val rDeo: OrderDao = INSTANCE!!.orderDao()
                    cDeo.deleteAll()
                    sDeo.deleteAll()
                    csDeo.deleteAll()
                    psDeo.deleteAll()
                    pDeo.deleteAll()
                    rDeo.deleteAll()
                }
            }
        }
    }

}