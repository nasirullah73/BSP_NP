package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.R
import com.bsp.orderbooking.adapter.OrderListAapter
import com.bsp.orderbooking.databinding.ActivityOrderListBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import com.google.gson.Gson

class OrderListActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderListBinding
    var adapter = OrderListAapter()
    val viewModel by viewModels<DataBaseViewModel>()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
        viewModel.getAllOders()
        viewModel.odderList.observeForever {
            adapter.list = it as ArrayList<OrderEntity>
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllOders()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_del -> {
                viewModel.ordersDel()
                viewModel.getAllOders()
                true
            }

            else -> {
                false
            }
        }
    }
}