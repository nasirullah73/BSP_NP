package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bsp.orderbooking.R
import com.bsp.orderbooking.database.DataBaseConnection
import com.bsp.orderbooking.databinding.ActivityHomeBinding
import com.bsp.orderbooking.db.entity.*
import com.bsp.orderbooking.events.*
import com.bsp.orderbooking.models.ServerData
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.App
import com.bsp.orderbooking.util.ProgressDialogUtils
import com.bsp.orderbooking.util.UserData
import com.bsp.orderbooking.util.UserData.simpleDateFormat
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import com.bsp.orderbooking.viewpager.ViewPagerAdapter
import com.opencsv.CSVWriter
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.*
import java.nio.channels.FileChannel
import java.sql.Connection
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    val viewModel by viewModels<DataBaseViewModel>()
    var saleList: List<ProductSale>? = null
    var todayTask = 0
    var luncherCustomer: ActivityResultLauncher<Intent>? = null
    var luncherProducts: ActivityResultLauncher<Intent>? = null
    var luncherSchdule: ActivityResultLauncher<Intent>? = null
    var luncherCusSch: ActivityResultLauncher<Intent>? = null

    var menuOffline: MenuItem? = null
    var menuOnline: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        binding.tabLayout.setupWithViewPager(binding.pagerView)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayFragment.newInstance(true), "Today Plan")
        adapter.addFragment(VistPlanFragment.newInstance(false), "All Plans")
/*        adapter.addFragment(OrderListFragment.newInstance(false), "Orders")
        adapter.addFragment(ProductsFragment.newInstance(false), "Products")
        adapter.addFragment(CustomersFragment.newInstance(false), "Customers")*/
        binding.pagerView.adapter = adapter

        viewModel.orderDetail()
        viewModel.orderDetail.observeForever {
            saleList = it
        }
        var dialog: SweetAlertDialog? = null
        viewModel.isSyncung.observeForever {
            if (it) {
                dialog = ProgressDialogUtils.createProgressDialog(this, "Syncing....", true)
                dialog?.show()
            } else {
                dialog?.dismissWithAnimation()
            }
        }


        luncherCustomer =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val dialog = ProgressDialogUtils.createProgressDialog(this)
                    dialog.show()
                    try {
                        // val filePath = result.data?.data?.path
                        val buffer = BufferedReader(
                            InputStreamReader(
                                contentResolver.openInputStream(result.data?.data!!)
                            )
                        )
                        var line = buffer.readLine()
                        line = buffer.readLine()
                        if (line != null) viewModel.deleteCustomer()
                        while (line != null) {
                            val data = line.split(",")
                            viewModel.insertCustomer(
                                Customer(
                                    _id = null,
                                    compcode = data[0],
                                    unitnumber = data[1],
                                    customerid = data[2],
                                    orgname = data[3],
                                    address = data[4],
                                    cityid = data[5],
                                    regionid = data[6],
                                    category = data[7],
                                    _class = data[8],
                                    ranking = data[9],
                                    phonenumber = data[10],
                                    mobilenumber = data[11],
                                    licensenumber9 = data[12],
                                    licenseexpiry9 = data[13],
                                    licensenumber11 = data[14],
                                    licenseexpiry11 = data[15],
                                    latitude = data[16],
                                    longitude = data[17]
                                )
                            )
                            line = buffer.readLine()
                        }
                        if (line == null) {
                            ProgressDialogUtils.showSuccess(
                                dialog,
                                "Customers",
                                "Saved Successfully"
                            )
                            EventBus.getDefault().post(RefreshCustomers())
                        }
                    } catch (e: Exception) {
                        ProgressDialogUtils.showError(dialog, "Customers", "Not Sved, Try again")
                    }

                }
            }
        luncherProducts =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val dialog = ProgressDialogUtils.createProgressDialog(this)
                    dialog.show()
                    try {
                        //  val filePath = result.data?.data?.path
                        val buffer = BufferedReader(
                            InputStreamReader(
                                contentResolver.openInputStream(result.data?.data!!)
                            )
                        )
                        var line = buffer.readLine()
                        line = buffer.readLine()
                        if (line != null) viewModel.deleteProducts()
                        while (line != null) {
                            val data = line.split(",")
                            viewModel.insertProducts(
                                ProductEntity(
                                    _id = null,
                                    col_branchid = if (data[0] != "") data[0].toInt() else 0,
                                    col_companyid = if (data[1] != "") data[1].toInt() else 0,
                                    col_spogroupid = if (data[2] != "") data[2].toInt() else 0,
                                    col_productid = if (data[3] != "") data[3].toString() else "",
                                    col_productname = data[4],
                                    col_unitsize = data[5],
                                    col_cartonsize = data[6],
                                    col_unitprice = if (data[9] != "") data[7].toDouble() else 0.0,
                                    col_retailprice = if (data[8] != "") data[8].toDouble() else 0.0,
                                    col_isshort = data[9],
                                    col_genericname = data[10]
                                )
                            )
                            line = buffer.readLine()
                        }
                        if (line == null) {
                            ProgressDialogUtils.showSuccess(
                                dialog,
                                "Products",
                                "Saved Successfully"
                            )
                            EventBus.getDefault().post(RefreshProducts())
                        }
                    } catch (e: Exception) {
                        ProgressDialogUtils.showError(dialog, "Products", "Not Sved, Try again")
                    }

                }
            }

        luncherSchdule = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dialog = ProgressDialogUtils.createProgressDialog(this)
                dialog.show()
                try {
                    // val filePath = result.data?.data?.path
                    val buffer = BufferedReader(
                        InputStreamReader(
                            contentResolver.openInputStream(result.data?.data!!)
                        )
                    )
                    var line = buffer.readLine()
                    line = buffer.readLine()
                    if (line != null) viewModel.deleteSchdule()
                    while (line != null) {
                        val data = line.split(",")
                        viewModel.insertSchdule(
                            Schedule(
                                _id = null,
                                col_employeeid = if (data[0] != "") data[0].toInt() else 0,
                                col_groupid = if (data[1] != "") data[1].toInt() else 0,
                                col_reegionid = if (data[2] != "") data[2].toInt() else 0,
                                col_groupname = data[3],
                                col_regiondesc = data[4],
                                col_weekday = data[5]
                            )
                        )
                        line = buffer.readLine()
                    }
                    if (line == null) {
                        ProgressDialogUtils.showSuccess(dialog, "Schadules", "Saved Successfully")
                        EventBus.getDefault().post(RefreshSchdule())
                    }
                } catch (e: Exception) {
                    ProgressDialogUtils.showError(dialog, "Schadules", "Not Sved, Try again")
                }

            }
        }

        luncherCusSch = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dialog = ProgressDialogUtils.createProgressDialog(this)
                dialog.show()
                try {
                    // val filePath = result.data?.data?.path
                    val buffer = BufferedReader(
                        InputStreamReader(
                            contentResolver.openInputStream(result.data?.data!!)
                        )
                    )
                    var line = buffer.readLine()
                    line = buffer.readLine()
                    if (line != null) viewModel.deleteCustomerSchdule()
                    while (line != null) {
                        val data = line.split(",")
                        viewModel.insertCustomerSchdule(
                            CustomerSchedule(
                                _id = null,
                                col_regionid = if (data[0] != "") data[0].toInt() else 0,
                                col_customerid = if (data[1] != "") data[1].toInt() else 0
                            )
                        )
                        line = buffer.readLine()
                    }
                    if (line == null) {
                        ProgressDialogUtils.showSuccess(
                            dialog,
                            "Customer Schadules",
                            "Saved Successfully"
                        )
                        EventBus.getDefault().post(RefreshToday())
                    }
                } catch (e: Exception) {
                    ProgressDialogUtils.showError(
                        dialog,
                        "Customer Schadules",
                        "Not Sved, Try again"
                    )
                }

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        menuOffline = menu?.findItem(R.id.action_offline)
        menuOnline = menu?.findItem(R.id.action_online)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_online -> {
                menuOnline?.isChecked = true
                menuOffline?.isChecked = false
                true
            }
            R.id.action_offline -> {
                menuOffline?.isChecked = true
                menuOnline?.isChecked = false
                true
            }
            R.id.action_orders -> {
                FrameActivity.startActivity(this, "Orders List", OrderListFragment.TAG)
                true
            }
            R.id.action_customers -> {
                FrameActivity.startActivity(this, "Customers List", CustomersFragment.TAG)
                true
            }
            R.id.action_products -> {
                FrameActivity.startActivity(this, "Products List", ProductsFragment.TAG)
                true
            }

            R.id.action_import_cus -> {
                if (menuOffline?.isChecked == true) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    luncherCustomer?.launch(Intent.createChooser(intent, "Select Customer File"))
                } else {
                    object : DataBaseConnection() {
                        override fun onPostExecute(connection: Connection) {
                            lifecycleScope.launch {
                                binding.syncView.isVisible = true
                                val list = getCustomer(connection)
                                if (list.size > 0) viewModel.deleteCustomer()
                                list.forEachIndexed { index, customer ->
                                    viewModel.insertCustomer(customer)
                                    binding.syncText.text = "Syncing..  (${index+1} / ${list.size})"
                                }
                                binding.syncView.isVisible = false
                            }
                        }

                        override fun onError(connection: String) {
                            binding.syncView.isVisible = false
                        }
                    }.execute(UserData.getIP(App.context))
                }

                EventBus.getDefault().post(RefreshCustomers())
                EventBus.getDefault().post(RefreshDashBord())
                true
            }
            R.id.action_import_pro -> {
                if (menuOffline?.isChecked == true) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    luncherProducts?.launch(Intent.createChooser(intent, "Select Product File"))
                } else {
                    object : DataBaseConnection() {
                        override fun onPostExecute(connection: Connection) {
                            lifecycleScope.launch {
                                binding.syncView.isVisible = true
                                val list = getProducts(connection)
                                if (list.size > 0) viewModel.deleteProducts()
                                list.forEachIndexed { index, productEntity ->
                                    viewModel.insertProducts(productEntity)
                                    binding.syncText.text = "Syncing..  (${index+1} / ${list.size})"
                                }
                                binding.syncView.isVisible = false
                            }
                        }

                        override fun onError(connection: String) {
                            binding.syncView.isVisible = false
                        }
                    }.execute(UserData.getIP(App.context))
                }

                EventBus.getDefault().post(RefreshCustomers())
                EventBus.getDefault().post(RefreshDashBord())
                true
            }
            R.id.action_import_sch -> {
                if (menuOffline?.isChecked == true) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    luncherSchdule?.launch(Intent.createChooser(intent, "Select Schdule File"))
                } else {
                    object : DataBaseConnection() {
                        override fun onPostExecute(connection: Connection) {
                            lifecycleScope.launch {
                                binding.syncView.isVisible = true
                                val list = getSchdule(connection)
                                if (list.size > 0) viewModel.deleteSchdule()
                                list.forEachIndexed { index, schedule ->
                                    viewModel.insertSchdule(schedule)
                                    binding.syncText.text = "Syncing..  (${index+1} / ${list.size})"
                                }
                                binding.syncView.isVisible = false
                            }
                        }

                        override fun onError(connection: String) {
                            binding.syncView.isVisible = false
                        }
                    }.execute(UserData.getIP(App.context))
                }

                EventBus.getDefault().post(RefreshCustomers())
                EventBus.getDefault().post(RefreshDashBord())
                true
            }
            R.id.action_import_cus_suh -> {
                if (menuOffline?.isChecked == true) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    luncherCusSch?.launch(
                        Intent.createChooser(
                            intent,
                            "Select Customer Schdule File"
                        )
                    )
                } else {
                    object : DataBaseConnection() {
                        override fun onPostExecute(connection: Connection) {
                            lifecycleScope.launch {
                                binding.syncView.isVisible = true
                                val list = getCustomerSchdule(connection)
                                if (list.size > 0) viewModel.deleteCustomerSchdule()
                                list.forEachIndexed { index, customerSchedule ->
                                    viewModel.insertCustomerSchdule(customerSchedule)
                                    binding.syncText.text = "Syncing..  (${index+1} / ${list.size})"
                                }
                                binding.syncView.isVisible = false
                            }
                        }

                        override fun onError(connection: String) {
                            binding.syncView.isVisible = false
                        }
                    }.execute(UserData.getIP(App.context))
                }

                EventBus.getDefault().post(RefreshCustomers())
                EventBus.getDefault().post(RefreshDashBord())
                true
            }

            R.id.action_ip -> {
                ProgressDialogUtils.dialodForIPAddress(this@HomeActivity,
                    object : ProgressDialogUtils.DialogSubmitClick {
                        override fun onClick(ip: ServerData) {
                            UserData.saveIP(this@HomeActivity, ip)
                        }
                    })
                true
            }

            R.id.action_upload -> {
                val dialog = ProgressDialogUtils.createProgressDialog(this)
                dialog.show()
                if (!saleList.isNullOrEmpty()) {
                    object : DataBaseConnection() {
                        override fun onPostExecute(connection: Connection) {
                            lifecycleScope.launch {
                                saleList?.forEachIndexed { index, productSale ->
                                    val result = saveOrder(connection, productSale)
                                    if (result == "Uploaded") {
                                        ProgressDialogUtils.showSuccess(
                                            dialog,
                                            "Orders",
                                            "${index + 1} / ${saleList?.size} Uploaded"
                                        )
                                        productSale.col_status = "U"
                                        viewModel.database?.productSaleDao()?.update(productSale)
                                    } else {
                                        ProgressDialogUtils.showSuccess(dialog, "Order", "Uploaded")
                                    }
                                }
                            }
                        }

                        override fun onError(connection: String) {
                            ProgressDialogUtils.showSuccess(dialog, "Order", "Uploaded")
                        }
                    }.execute(UserData.getIP(App.context))
                } else {
                    ProgressDialogUtils.showError(dialog, "Order", "No Pending Orders")
                }
                true
            }

            R.id.action_eport -> {
                if (!saleList.isNullOrEmpty()) {
                    saveFile(saleList as List<ProductSale>)
                } else {
                    Toast.makeText(this, "No Pending Order", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_del -> {
                viewModel.ordersDel()
                EventBus.getDefault().post(RefreshCustomers())
                EventBus.getDefault().post(RefreshDashBord())
                true
            }
            R.id.action_backup -> {
                exportDatabse("BspAppDatabase2")
                true
            }
            R.id.action_log_out -> {
                UserData.deleteUser(this)
                ActivityUtils.startLogInActivity(this)
                finish()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onBackPressed() {
        when (binding.tabLayout.selectedTabPosition) {
            0 -> {
                super.onBackPressed()
            }
            else -> {
                binding.tabLayout.getTabAt(0)?.select()
            }
        }
    }

    @SuppressLint("NewApi")
    fun saveFile(list: List<ProductSale>) {
        val dialog = ProgressDialogUtils.createProgressDialog(this)
        dialog.show()
        val path = Environment.getExternalStorageDirectory()
        val drive = File(path.absolutePath, "BSP")
        if (!drive.exists()) {
            drive.mkdirs()
        }
        val file = File(drive, "orderlist_date_${simpleDateFormat.format(Date())}.csv")
        try {
            file.parentFile.mkdirs()
            file.createNewFile()
            val cWritter = CSVWriter(FileWriter(file))
            val colums = listOf(
                ProductSale.COL_BOOKINGDATE,
                ProductSale.COL_UPLOADDATE,
                ProductSale.COL_CUSTOMERID,
                ProductSale.COL_PORDUCTID,
                ProductEntity.COL_PRODUCTNAME,
                ProductSale.COL_QTY,
                ProductEntity.COL_RETAILPRICE,
                ProductSale.COL_STATUS,
                ProductSale.COL_USERID,
                ProductSale.COL_SALEID,
                ProductSale.COL_EXPORT_STATUS,
                ProductSale.COL_MID,
                ProductSale.COL_REASON,
                ProductSale.COL_LATITUDE,
                ProductSale.COL_LONGITUDE,
                ProductSale.COL_DATASOURCE,
                ProductSale.COL_CLOUDUPLOADSTATUS
            )
            cWritter.writeNext(colums.toTypedArray())

            list.forEach {
                val item = listOf(
                    it.col_bookingdate,
                    it.col_uploaddate,
                    it.col_customerid,
                    it.col_porductid,
                    it.col_productname,
                    it.col_qty?.toString(),
                    it.col_retailprice?.toString(),
                    it.col_status,
                    it.col_userid?.toString(),
                    it.col_saleid,
                    it.col_export_status,
                    it.col_mid,
                    it.col_reason,
                    it.latitude,
                    it.longitude,
                    it.col_datasource,
                    it.col_clouduploadstatus
                )
                cWritter.writeNext(item.toTypedArray())
            }

            cWritter.close()
            Log.d("File Path", "$drive")
            ProgressDialogUtils.showSuccess(
                dialog, "Saved Orders", "File Path :\n $drive/orderlist_date_${
                    simpleDateFormat.format(
                        Date()
                    )
                }.csv"
            )
            //   exportDatabse("BspAppDatabase2")
        } catch (e: Exception) {
            Log.d("Error : ", "$e")
            ProgressDialogUtils.showError(dialog, "Error", "$e")
        }
    }

    fun exportDatabse(databaseName: String) {
        val dialog = ProgressDialogUtils.createProgressDialog(this)
        dialog.show()
        try {
            val sd = Environment.getExternalStorageDirectory()
            val drive = File(sd.absolutePath, "BSP")
            if (!drive.exists()) {
                drive.mkdirs()
            }
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = "//data//$packageName//databases//$databaseName"
                val backupDBPath = "DataBaseBackUp.sqlite"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(drive, backupDBPath)
                if (currentDB.exists()) {
                    val src: FileChannel = FileInputStream(currentDB).channel
                    val dst: FileChannel = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                }
            }
            Log.d("File Path", "$drive")
            Log.d("File Path", "$data")
            ProgressDialogUtils.showSuccess(
                dialog,
                "Back Up Completely Saved",
                "Address: \nBSP/DataBaseBackUp.sqlite"
            )
        } catch (e: java.lang.Exception) {
            ProgressDialogUtils.showError(dialog, "Sorry", "Please! Try Again")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.orderDetail()
    }


}