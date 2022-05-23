package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ActivitySyncBinding
import com.bsp.orderbooking.events.*
import com.bsp.orderbooking.util.ProgressDialogUtils
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus

class SyncActivity : AppCompatActivity() {
    val viewModel by viewModels<DataBaseViewModel>()
    lateinit var binding : ActivitySyncBinding
    var dialog : SweetAlertDialog? = null

    @SuppressLint("NewApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customers.setOnClickListener {
            binding.finish.setBackgroundColor(getColor(R.color.Red))
            binding.finish.text = "Cancel Sync"
            viewModel.syncCustomer()
            showdialog()
            Handler(Looper.getMainLooper()).postDelayed({
                finish  = true
                binding.finish.setBackgroundColor(getColor(R.color.Green))
                binding.finish.text = "Goto Home Screen"
                dismisDailod()
            }, (15000).toLong())
            EventBus.getDefault().post(RefreshCustomers())
        }
        binding.products.setOnClickListener {
            binding.finish.setBackgroundColor(getColor(R.color.Red))
            binding.finish.text = "Cancel Sync"
            viewModel.syncProducts()
            showdialog()
            Handler(Looper.getMainLooper()).postDelayed({
                finish  = true
                binding.finish.setBackgroundColor(getColor(R.color.Green))
                binding.finish.text = "Goto Home Screen"
                dismisDailod()
            }, (15000).toLong())
            EventBus.getDefault().post(RefreshProducts())
        }
        binding.schdule.setOnClickListener {
            binding.finish.setBackgroundColor(getColor(R.color.Red))
            binding.finish.text = "Cancel Sync"
            viewModel.syncSchdule()
            showdialog()
            Handler(Looper.getMainLooper()).postDelayed({
                finish  = true
                binding.finish.setBackgroundColor(getColor(R.color.Green))
                binding.finish.text = "Goto Home Screen"
                dismisDailod()
            }, (15000).toLong())
            EventBus.getDefault().post(RefreshSchdule())
        }
        binding.cussch.setOnClickListener {
            binding.finish.setBackgroundColor(getColor(R.color.Red))
            binding.finish.text = "Cancel Sync"
            viewModel.syncSchCus()
            showdialog()
            Handler(Looper.getMainLooper()).postDelayed({
                finish  = true
                binding.finish.setBackgroundColor(getColor(R.color.Green))
                binding.finish.text = "Goto Home Screen"
                dismisDailod()
            }, (15000).toLong())
            EventBus.getDefault().post(RefreshToday())
        }
        binding.finish.setOnClickListener {
            if (!finish){
                Snackbar.make(binding.root, "Cancel Syncing ..?", Snackbar.LENGTH_LONG)
                    .setAction("YES") {
                        EventBus.getDefault().post(RefreshSchdule())
                        EventBus.getDefault().post(RefreshToday())
                        finish()
                    }.show()
            }else{
                finish()
            }
        }
    }

    var finish = false
    override fun onBackPressed() {
        if(finish) super.onBackPressed()
        else {
            Snackbar.make(binding.root, "Cancel Syncing ..?", Snackbar.LENGTH_LONG)
                .setAction("YES") {
                    EventBus.getDefault().post(RefreshSchdule())
                    EventBus.getDefault().post(RefreshToday())
                    finish()
                }.show()
        }
    }

    fun showdialog(){
        dialog =ProgressDialogUtils.createProgressDialog(this)
        dialog!!.show()
    }

    fun dismisDailod(){
        dialog!!.dismiss()
    }
}