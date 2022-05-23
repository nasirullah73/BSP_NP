package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.R
import com.bsp.orderbooking.adapter.CustomersAapter
import com.bsp.orderbooking.databinding.ActivityCustomersBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.events.RefreshCustomers
import com.bsp.orderbooking.models.Chemist
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*


class CustomersActivity : AppCompatActivity() {
    lateinit var binding: ActivityCustomersBinding
    val viewModel by viewModels<DataBaseViewModel>()
    var listCustomers: ArrayList<Customer> = ArrayList()
    var orderCustomers: ArrayList<OrderEntity> = ArrayList()
    var chemistList: ArrayList<Chemist> = ArrayList()
    var adapter: CustomersAapter? = null
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        viewModel.getAllOders()
        viewModel.odderList.observeForever {
            adapter?.orderList = it as ArrayList<OrderEntity>
            adapter?.notifyDataSetChanged()
        }

        val isEnable = intent.getBooleanExtra("isFirst", false)
        adapter = CustomersAapter(isEnable)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter

        val rid = intent.getIntExtra("rid", -1)
        viewModel.getCusTomerByRegionId(rid)
        viewModel.customerList.observeForever {
            listCustomers = it as ArrayList<Customer>
            adapter?.list = it
            binding.emptyView.isVisible = it.isEmpty()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshCustomers) {
        viewModel.getAllOders()
    }

}