package com.bsp.orderbooking.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsp.orderbooking.db.database.AppDatabase
import com.bsp.orderbooking.db.entity.*
import com.bsp.orderbooking.models.Chemist
import com.bsp.orderbooking.network.ApiAdapter
import com.bsp.orderbooking.util.App
import com.bsp.orderbooking.util.UserData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class DataBaseViewModel : ViewModel() {
    var database = AppDatabase.getDatabase(App.context)
    val isSyncung = MutableLiveData<Boolean>()


    fun insertCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.customerDao().insert(customer)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
    }

    fun insertOrder(customer: OrderEntity, orderList: ArrayList<ProductSale>): Int {
        var id = 0
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.orderDao().insert(customer)
                    id = database!!.orderDao().getID()

                    orderList.forEach {
                        it.col_orderid = id.toString()
                        insertProductSale(it)
                    }
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
        return id
    }

    fun updateOrder(customer: OrderEntity) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.orderDao().update(customer)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
    }

    fun insertProducts(product: ProductEntity) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.productDao().insert(product)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
    }

    fun insertSchdule(schedule: Schedule) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.schduleDao().insert(schedule)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
    }

    fun insertCustomerSchdule(customerSchedule: CustomerSchedule) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.customerSchduleDao().insert(customerSchedule)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
    }

    fun insertProductSale(productSale: ProductSale) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.productSaleDao().insert(productSale)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data inserted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data inserted")
            }
        }
    }

    fun deleteProductSale(id: Int) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.productSaleDao().delete(id)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data Deleted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data Deleted")
            }
        }
    }

    fun updateProductSale(productSale: ProductSale) {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.productSaleDao().update(productSale)
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data Updated")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data Update")
            }
        }
    }


    @SuppressLint("NewApi")
    fun deleteCustomer() {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.customerDao().deleteAll()
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data deleted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data deleted")
            }
        }
    }

    @SuppressLint("NewApi")
    fun deleteProducts() {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.productDao().deleteAll()
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data deleted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data deleted")
            }
        }
    }

    @SuppressLint("NewApi")
    fun deleteSchdule() {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.schduleDao().deleteAll()
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data deleted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data deleted")
            }
        }
    }


    @SuppressLint("NewApi")
    fun deleteCustomerSchdule() {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.customerSchduleDao().deleteAll()
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data deleted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data deleted")
            }
        }
    }

    @SuppressLint("NewApi")
    fun deleteProductSale() {
        viewModelScope.launch {
            try {
                CompositeDisposable().add(Observable.fromCallable {
                    database!!.productSaleDao().deleteAll()
                }
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        Log.d("response", "Data deleted")
                    })
            } catch (e: Exception) {
                Log.d("response", "Not data deleted")
            }
        }
    }


    var customerList = MutableLiveData<List<Customer>?>()
    var schduleList = MutableLiveData<List<Schedule>?>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        // loadCustomers()
        loadSchdule()
        isLoading.value = false
    }

    fun loadCustomers() {
        viewModelScope.launch {
            customerList.value = database!!.customerDao().getAll()
        }
    }

    fun loadSchdule() {
        viewModelScope.launch {
            schduleList.value =
                database!!.schduleDao().getAllByEmpId(UserData.getUser(App.context).id ?: -1)
        }
    }

    fun ordersDel() {
        viewModelScope.launch {
            database!!.orderDao().deleteAll()
            database!!.productSaleDao().deleteAll()
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            database!!.productDao().deleteAll()
            database!!.customerSchduleDao().deleteAll()
            database!!.customerDao().deleteAll()
            database!!.schduleDao().deleteAll()
            database!!.orderDao().deleteAll()
            database!!.productSaleDao().deleteAll()
        }
    }

    fun getCustomerByID(id: Int): Customer? {
        return database!!.customerDao().getById(id)
    }


    suspend fun getCustimers(url: String?) {
        database!!.customerDao().deleteAll()
        ApiAdapter.apiClient.getCustomers(url).also { it1 ->
            when {
                it1.isSuccessful -> {
                    val data = it1.body()?.data
                    val list: ArrayList<Customer> = ArrayList()
                    data?.forEach { data1 ->
                        list.add(
                            Customer(
                                null,
                                data1.customerid.toString(),
                                data1.unitnumber.toString(),
                                data1.customerid.toString(),
                                data1.orgname,
                                data1.address,
                                data1.cityid.toString(),
                                data1.regionid.toString(),
                                data1.category,
                                data1.class_,
                                data1.ranking,
                                data1.phonenumber,
                                data1.mobilenumber,
                                data1.licensenumber9,
                                data1.licenseexpiry9,
                                data1.licensenumber11,
                                data1.licenseexpiry11,
                                data1.latitude,
                                data1.longitude
                            )
                        )
                    }
                    list.forEach {
                        insertCustomer(it)
                    }
                }
                else -> {
                }
            }
        }
    }

    suspend fun getProducts(url: String?) {
        database!!.productDao().deleteAll()
        ApiAdapter.apiClient.getProducts(url).also { response ->
            when {
                response.isSuccessful -> {
                    val data = response.body()?.data
                    val list: ArrayList<ProductEntity> = ArrayList()
                    data!!.forEach {
                        list.add(
                            ProductEntity(
                                null,
                                it.branchid,
                                it.companyid,
                                it.spogroupid,
                                it.productid?.toString(),
                                it.productname,
                                it.unitsize,
                                it.cartonsize,
                                it.unitprice,
                                it.retailprice,
                                it.isshort,
                                it.genericname
                            )
                        )
                    }
                    list.forEach {
                        insertProducts(it)
                    }
                }
                else -> {
                }
            }
        }
    }

    suspend fun getSchdule(url: String?) {
        database!!.schduleDao().deleteAll()
        ApiAdapter.apiClient.getSchdule(url).also { response ->
            when {
                response.isSuccessful -> {
                    val data = response.body()?.data
                    val list: ArrayList<Schedule> = ArrayList()
                    data!!.forEach {
                        list.add(
                            Schedule(
                                null,
                                it.employeeid,
                                it.groupid,
                                it.regionid,
                                it.groupname,
                                it.regiondesc,
                                it.weekday
                            )
                        )
                    }
                    list.forEach {
                        insertSchdule(it)
                    }
                }
                else -> {
                }
            }
        }
    }

    suspend fun getCusSch(url: String?) {
        database!!.customerSchduleDao().deleteAll()
        ApiAdapter.apiClient.getSchduleCustomer(url).also { response ->
            when {
                response.isSuccessful -> {
                    val data = response.body()?.data
                    val list: ArrayList<CustomerSchedule> = ArrayList()
                    data!!.forEach {
                        list.add(
                            CustomerSchedule(
                                null,
                                it.regionid,
                                it.customerid
                            )
                        )
                    }
                    list.forEach {
                        insertCustomerSchdule(it)
                    }
                }
                else -> {
                }
            }

        }
    }

    fun syncCustomer() {
        isSyncung.value = true
        viewModelScope.launch {
            ApiAdapter.apiClient.getUrls().also {
                when {
                    it.isSuccessful -> {
                        try {
                            it.body()?.data?.forEach { it1 ->
                                if (it1.companyid == UserData.getUser(App.context).comp) {
                                    awaitAll(
                                        this.async(Dispatchers.IO) {
                                            getCustimers(it1.customor.toString())
                                        }
                                    )
                                    isSyncung.value = true
                                }
                            }
                        } catch (e: Exception) {
                            Log.d("response", "Error :$e")
                        }
                    }
                    else -> {
                    }
                }
            }

        }
    }

    fun syncProducts() {
        isSyncung.value = true
        viewModelScope.launch {
            ApiAdapter.apiClient.getUrls().also {
                when {
                    it.isSuccessful -> {
                        try {
                            it.body()?.data?.forEach { it1 ->
                                if (it1.companyid == UserData.getUser(App.context).comp) {

                                    awaitAll(
                                        this.async(Dispatchers.IO) {
                                            getProducts(it1.products)
                                        }
                                    )
                                }
                            }
                            isSyncung.value = false
                        } catch (e: Exception) {
                            isSyncung.value = false
                            Log.d("response", "Error :$e")
                        }
                    }
                    else -> {
                        isSyncung.value = false
                    }
                }
            }

        }
    }

    fun syncSchdule() {
        isSyncung.value = true
        viewModelScope.launch {
            ApiAdapter.apiClient.getUrls().also {
                when {
                    it.isSuccessful -> {
                        try {
                            it.body()?.data?.forEach { it1 ->
                                if (it1.companyid == UserData.getUser(App.context).comp) {

                                    awaitAll(
                                        async(Dispatchers.IO) {
                                            getSchdule(it1.schdule)
                                        }
                                    )
                                }
                            }
                            isSyncung.value = false
                        } catch (e: Exception) {
                            Log.d("response", "Error :$e")
                        }
                        isLoading.value = false
                    }
                    else -> {
                        isLoading.value = false
                    }
                }
            }

        }
    }

    fun syncSchCus() {
        isSyncung.value = true
        viewModelScope.launch {
            ApiAdapter.apiClient.getUrls().also {
                when {
                    it.isSuccessful -> {
                        try {
                            it.body()?.data?.forEach { it1 ->
                                if (it1.companyid == UserData.getUser(App.context).comp) {

                                    awaitAll(
                                        this.async(Dispatchers.IO) {
                                            getCusSch(it1.custmerSchdule)
                                        }
                                    )
                                }
                            }
                            isSyncung.value = false
                        } catch (e: Exception) {
                            Log.d("response", "Error :$e")
                            isSyncung.value = false
                        }
                    }
                    else -> {
                        isSyncung.value = false
                    }
                }
            }

        }
    }

    val chemistList = MutableLiveData<List<Chemist>?>()
    fun getCusTomerByRegionId(id: Int?) {
        val list: ArrayList<Customer> = ArrayList()
        viewModelScope.launch {
            database!!.customerSchduleDao().getAllByRegId(id).forEach {
                val c = database!!.customerDao().getById(it.col_customerid)
                if (c != null) {
                    list.add(c)
                }
            }
            customerList.value = list
        }
    }

    val productsList = MutableLiveData<List<ProductEntity>?>()
    fun getAllProducts() {
        isLoading.value = true
        viewModelScope.launch {
            productsList.value = database!!.productDao().getAll()
            isLoading.value = false
        }
    }

    val odderList = MutableLiveData<List<OrderEntity>?>()
    fun getAllOders() {
        isLoading.value = true
        viewModelScope.launch {
            odderList.value = database!!.orderDao().getAll()
            isLoading.value = false
            Log.d("Orders: ", Gson().toJson(odderList))
        }
    }

    val orderDetailList = MutableLiveData<List<ProductSale>?>()
    fun orderDetailbyid(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            orderDetailList.value = database!!.productSaleDao().getAllById(id)
            isLoading.value = false
        }
    }

    val orderDetail = MutableLiveData<List<ProductSale>?>()
    fun orderDetail() {
        viewModelScope.launch {
            orderDetail.value = database!!.productSaleDao().getAll()
        }
    }

    fun getProductByName(name: String): ProductEntity? {
        return database?.productDao()?.getByName(name)
    }


}