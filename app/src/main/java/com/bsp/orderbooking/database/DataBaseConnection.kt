package com.bsp.orderbooking.database

import android.annotation.SuppressLint
import android.util.Log
import com.bsp.orderbooking.db.entity.*
import com.bsp.orderbooking.models.ServerData
import com.bsp.orderbooking.models.UserInfo
import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

abstract class DataBaseConnection : CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    fun calcel() {
        job.cancel()
    }

    suspend fun doInBackground(ip: ServerData): Connection = withContext(Dispatchers.IO) {
        return@withContext createConnection(ip)!!
    }

    fun execute(ip: ServerData) = launch {
        try {
            val result = doInBackground(ip)
            Log.e("Sql ", "${result.metaData.userName}")
            onPostExecute(result)
        } catch (e: Exception) {
            Log.e("Sql Error", "$e")
            onError(e.toString())
        }
    }

    protected abstract fun onPostExecute(connection: Connection)
    protected abstract fun onError(connection: String)


    @Throws(ClassNotFoundException::class, SQLException::class)
    fun createConnection(ip: ServerData): Connection? {
        return createConnection(
            DEFULT_DRIVER,
            "${DEFULT_URL}${ip.ip}:${PORT}:${ip.database}",
            ip.user,
            ip.password
        )
    }

    @Throws(ClassNotFoundException::class, SQLException::class)
    fun createConnection(
        drive: String, url: String, user: String, password: String
    ): Connection? {
        Class.forName(drive)
        return DriverManager.getConnection(url, user, password)
    }

    companion object {
        val DEFULT_DRIVER = "oracle.jdbc.driver.OracleDriver" //"oracle.jdbc.OracleDriver"
        val DEFULT_URL =
            "jdbc:oracle:thin:@"/*"jdbc:oracle:thin:@192.168.43.167:xe"*/
        val IP_ADDRESS = "192.168.43.167"
        val PORT = "1521"
        val DATABASE_NAME = "BSP"
        val USER = "bsp"
        val PASSWORD = "teachers"

        val USERS_QUERY = "select * from Users"
        val CUSTOMER_SCHDULE = "select * from v_area_detail"
        val SCHDULE = "select * from v_tour"
        val CUSTOMER = "select * from v_customers"
        val PRODUCTS = "select * from v_products"
        val INSERTORDERS = "INSERT INTO productsale VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

        suspend fun getUsers(connection: Connection): ArrayList<UserInfo> =
            withContext(Dispatchers.IO) {
                val list: ArrayList<UserInfo> = ArrayList()
                val stream = connection.createStatement()
                val execute = stream.execute(USERS_QUERY)
                if (execute) {
                    val data = stream.resultSet
                    while (data.next()) {
                        list.add(
                            UserInfo(
                                data.getString(4),
                                data.getString(5),
                                data.getInt(3),
                                data.getInt(1)
                            )
                        )
                    }
                }
                stream.close()
                return@withContext list
            }

        suspend fun getCustomerSchdule(connection: Connection): ArrayList<CustomerSchedule> =
            withContext(Dispatchers.IO)
            {
                val list: ArrayList<CustomerSchedule> = ArrayList()
                val stream = connection.createStatement()
                val execute = stream.execute(CUSTOMER_SCHDULE)
                if (execute) {
                    val data = stream.resultSet
                    while (data.next()) {
                        list.add(
                            CustomerSchedule(null, data.getInt(1), data.getInt(2))
                        )
                    }
                }
                stream.close()
                return@withContext list
            }

        suspend fun getCustomer(connection: Connection): ArrayList<Customer> =
            withContext(Dispatchers.IO)
            {
                val list: ArrayList<Customer> = ArrayList()
                val stream = connection.createStatement()
                val execute = stream.execute(CUSTOMER)
                if (execute) {
                    val data = stream.resultSet
                    while (data.next()) {
                        list.add(
                            Customer(
                                null,
                                data.getString(1),
                                data.getString(2),
                                data.getString(3),
                                data.getString(4),
                                data.getString(5),
                                data.getString(6),
                                data.getString(7),
                                data.getString(8),
                                data.getString(9),
                                data.getString(10),
                                data.getString(11),
                                data.getString(12),
                                data.getString(13),
                                data.getString(14),
                                data.getString(15),
                                data.getString(16),
                                data.getString(17),
                                data.getString(18)
                            )
                        )
                    }
                }
                stream.close()
                return@withContext list
            }

        suspend fun getSchdule(connection: Connection): ArrayList<Schedule> =
            withContext(Dispatchers.IO)
            {
                val list: ArrayList<Schedule> = ArrayList()
                val stream = connection.createStatement()
                val execute = stream.execute(SCHDULE)
                if (execute) {
                    val data = stream.resultSet
                    while (data.next()) {
                        list.add(
                            Schedule(
                                null,
                                data.getInt(1),
                                data.getInt(2),
                                data.getInt(3),
                                data.getString(4),
                                data.getString(5),
                                data.getString(6)
                            )
                        )
                    }
                }
                stream.close()
                return@withContext list
            }


        suspend fun getProducts(connection: Connection): ArrayList<ProductEntity> =
            withContext(Dispatchers.IO)
            {
                val list: ArrayList<ProductEntity> = ArrayList()
                val stream = connection.createStatement()
                val execute = stream.execute(PRODUCTS)
                if (execute) {
                    val data = stream.resultSet
                    while (data.next()) {
                        list.add(
                            ProductEntity(
                                null,
                                data.getInt(1),
                                data.getInt(2),
                                data.getInt(3),
                                data.getString(4),
                                data.getString(5),
                                data.getString(6),
                                data.getString(7),
                                data.getDouble(8),
                                data.getDouble(9),
                                data.getString(10),
                                data.getString(11)
                            )
                        )
                    }
                }
                stream.close()
                return@withContext list
            }

        @SuppressLint("NewApi", "SimpleDateFormat")
        suspend fun saveOrder(connection: Connection, productSale: ProductSale): String =
            withContext(Dispatchers.IO) {
                try {
                    val date =
                        SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(productSale.col_bookingdate)
                    val stream = connection.prepareStatement(INSERTORDERS)
                    val id = productSale.col_orderid + productSale.col_porductid + productSale._id + productSale.col_customerid + productSale.col_userid
                    stream.setInt(1, id.toInt())
                    stream.setTime(2, java.sql.Time(date?.time!!))
                    stream.setTime(3, java.sql.Time(Date().time))
                    stream.setInt(4, productSale.col_customerid?.toInt() ?: 0)
                    stream.setString(5, productSale.col_porductid ?: "")
                    stream.setInt(6, productSale.col_qty ?: 0)
                    stream.setString(7, "U")
                    stream.setString(8, productSale.col_userid?.toString() ?: "")
                    stream.setString(9, productSale.col_saleid ?: "")
                    stream.setString(10, "")
                    stream.setString(11, productSale.col_reason ?: "")
                    stream.setString(12, productSale.latitude ?: "")
                    stream.setString(13, productSale.longitude ?: "")
                    stream.setString(14, "C")
                    stream.setString(15, productSale.col_clouduploadstatus ?: "")
                    stream.executeUpdate()
                    Log.e("Saved Order", "$id")
                    return@withContext "Uploaded"
                } catch (e: Exception) {
                    return@withContext "Error: $e"
                }
            }
    }

}