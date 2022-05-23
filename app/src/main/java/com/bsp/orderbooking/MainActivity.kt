package com.bsp.orderbooking

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bsp.orderbooking.databinding.ActivityMainBinding
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.db.entity.ProductEntity.Companion.COL_PRODUCTNAME
import com.bsp.orderbooking.db.entity.ProductEntity.Companion.COL_RETAILPRICE
import com.bsp.orderbooking.db.entity.ProductSale
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_BOOKINGDATE
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_CLOUDUPLOADSTATUS
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_CUSTOMERID
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_DATASOURCE
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_EXPORT_STATUS
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_LATITUDE
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_LONGITUDE
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_MID
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_ORDERID
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_PID
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_PORDUCTID
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_QTY
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_REASON
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_SALEID
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_STATUS
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_UPLOADDATE
import com.bsp.orderbooking.db.entity.ProductSale.Companion.COL_USERID
import com.bsp.orderbooking.events.RefreshDashBord
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.ProgressDialogUtils
import com.bsp.orderbooking.util.UserData
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import com.google.gson.Gson
import com.opencsv.CSVWriter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    val viewModel by viewModels<DataBaseViewModel>()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("EEEE")

    var schID : Int? = null

    var orderList : List<OrderEntity>? = null
    var saleList : List<ProductSale>? = null

    val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)
        val user = UserData.getUser(this)
        binding.userNameTv.text =user.name?.uppercase()+"-" + user.id.toString()
       // binding.desTv.text = user.email

        val isFrest = intent.getBooleanExtra("isFirst",false)
        if(isFrest){
            val dialog = ProgressDialogUtils.createProgressDialog(this)
            dialog.show()
            ProgressDialogUtils.showSuccess(dialog,"Sync","Plase Sync Redord"
            ) {
                sync()
            }
        }

        binding.syncBtn.setOnClickListener {
            sync()
        }
        binding.visitLayout.setOnClickListener {
            ActivityUtils.startVisitsPlanActivity(this)
        }

        binding.chemistLayout.setOnClickListener {
            ActivityUtils.startCustomersActivity(this,schID ?: 0,true)
        }
        binding.orderListLayout.setOnClickListener {
            ActivityUtils.startOrderListActivity(this)
        }

        binding.regbuttonmain.setOnClickListener {
            UserData.deleteUser(this)
            ActivityUtils.startLogInActivity(this)
            finish()
        }
        viewModel.orderDetail.observeForever {
            saleList = it
        }
        viewModel.schduleList.observeForever { list ->
            val data = dateFormat.format(Date())
            list!!.forEach {
                if(data.lowercase().contains(it.col_weekday!!.lowercase())){
                    schID = it.col_reegionid
                    viewModel.getCusTomerByRegionId(schID)
                }
            }
        }
        viewModel.getAllOders()
        var tot = 1
        var ord = 0
        viewModel.customerList.observeForever {
            binding.chemistCont.text = it?.size?.toString()
            tot = it?.size ?: 1
            binding.progressTv.text = "${
                if(ord.toString().length ==1) "0$ord" else ord
            } / $tot"

            val pro = (ord*100) / tot
            binding.progress.progress = pro
            if(binding.countFile.text.toString() == "00"){
                binding.countFile.isVisible = false
            }
        }
        viewModel.odderList.observeForever {
            orderList = it as List<OrderEntity>
            ord = it.size
            binding.progressTv.text = "${
                if(ord.toString().length ==1) "0$ord" else ord
            } / $tot"
            binding.countFile.text = if(ord.toString().length ==1) "0$ord" else ord.toString()
            val pro = (ord*100) / tot
            binding.progress.progress = pro
            if(binding.countFile.text.toString() == "00"){
                binding.countFile.isVisible = false
            }
        }
        viewModel.orderDetail()
        binding.orderUploadLayout.setOnClickListener {
            if(binding.countFile.text.toString() != "00"){
                saveFile(saleList as List<ProductSale>)
            }else{
                Toast.makeText(this,"No Pending Order",Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun sync(){
        ActivityUtils.startSyncActivity(this)
    }

    @SuppressLint("NewApi")
    fun saveFile(list : List<ProductSale>){
        val dialog = ProgressDialogUtils.createProgressDialog(this)
        dialog.show()
        val path  = Environment.getExternalStorageDirectory()
        val drive = File(path.absolutePath ,"BSP")
        if(!drive.exists()){
            drive.mkdirs()
        }
        val file  = File(drive,"orderlist_date_${simpleDateFormat.format(Date())}.csv")
        if(file.exists()){
            file.delete()
        }
        try{
            file.createNewFile()
            val cWritter = CSVWriter(FileWriter(file))
            val colums = listOf(
                COL_BOOKINGDATE,
                        COL_UPLOADDATE ,
                        COL_CUSTOMERID ,
                        COL_PORDUCTID ,
                        COL_PRODUCTNAME,
                        COL_QTY ,
                        COL_RETAILPRICE,
                        COL_STATUS ,
                        COL_USERID,
                        COL_SALEID ,
                        COL_EXPORT_STATUS ,
                        COL_MID ,
                        COL_REASON ,
                        COL_LATITUDE ,
                        COL_LONGITUDE ,
                        COL_DATASOURCE ,
                        COL_CLOUDUPLOADSTATUS,
                        COL_PID,
                        COL_ORDERID
            )
            cWritter.writeNext(colums.toTypedArray())

            list.forEach {
                val item = listOf(
                    it.col_bookingdate,
                    it.col_uploaddate ,
                    it.col_customerid ,
                    it.col_porductid ,
                    it.col_productname,
                    it.col_qty?.toString() ,
                    it.col_retailprice?.toString(),
                    it.col_status ,
                    it.col_userid?.toString(),
                    it.col_saleid ,
                    it.col_export_status ,
                    it.col_mid ,
                    it.col_reason ,
                    it.latitude ,
                    it.longitude ,
                    it.col_datasource ,
                    it.col_clouduploadstatus,
                    it.col_pid,
                    it.col_orderid
                )
                cWritter.writeNext(item.toTypedArray())
            }

            cWritter.close()
           /* Files.createFile(file.toPath())
            val filestreem = FileWriter(file)
            filestreem.append("[")
            list.forEachIndexed { index, productSale ->
                filestreem.append(
                    Gson().toJson(productSale).toString()
                )
                if(index != list.size-1) filestreem.append(",")
            }
            filestreem.append("]")
            filestreem.flush()
            filestreem.close()
           */ Log.d("File Path", "$drive")
            ProgressDialogUtils.showSuccess(dialog,"Saved Orders","File Path :\n $drive/orderlist_date_${simpleDateFormat.format(Date())}.csv")
         //   exportDatabse("BspAppDatabase2")
        }catch (e:Exception){
            Log.d("Error : ", "$e")
            ProgressDialogUtils.showError(dialog,"Error","$e")
        }
    }
    fun exportDatabse(databaseName: String) {
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = "//data//$packageName//databases//$databaseName"
                val backupDBPath = "BspAppDatabase2.db"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(sd, backupDBPath)
                if (currentDB.exists()) {
                    val src: FileChannel = FileInputStream(currentDB).channel
                    val dst: FileChannel = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                }
            }
            Log.d("File Path", "$sd")
            Log.d("File Path", "$data")
        } catch (e: java.lang.Exception) {
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshDashBord) {
        viewModel.getAllOders()
        viewModel.loadSchdule()
    }

}