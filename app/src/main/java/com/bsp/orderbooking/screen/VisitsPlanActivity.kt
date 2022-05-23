package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.R
import com.bsp.orderbooking.adapter.CustomersListAapter
import com.bsp.orderbooking.databinding.ActivityVisitsPlanBinding
import com.bsp.orderbooking.db.entity.Schedule
import com.bsp.orderbooking.viewModel.DataBaseViewModel

class VisitsPlanActivity : AppCompatActivity() {
    lateinit var binding: ActivityVisitsPlanBinding
    var adapter = CustomersListAapter()
    val viewModel by viewModels<DataBaseViewModel>()
    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitsPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter

        viewModel.schduleList.observeForever {
            adapter.list = it as ArrayList<Schedule>
        }
    }
}