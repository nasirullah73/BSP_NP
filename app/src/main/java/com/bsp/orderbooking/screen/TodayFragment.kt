package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.adapter.CustomersAapter
import com.bsp.orderbooking.databinding.ListLayoutBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.events.RefreshToday
import com.bsp.orderbooking.models.Chemist
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment() {

    var _binding: ListLayoutBinding? = null
    val binding get() = _binding!!

    var isEnable: Boolean? = null
    val viewModel by viewModels<DataBaseViewModel>()
    var listCustomers: ArrayList<Customer> = ArrayList()
    var orderCustomers: ArrayList<OrderEntity> = ArrayList()
    var chemistList: ArrayList<Chemist> = ArrayList()
    var adapter: CustomersAapter? = null
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("EEEE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEnable = it.getBoolean("isFirst")
        }
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListLayoutBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearLayout.isVisible = true
        adapter = CustomersAapter(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        viewModel.schduleList.observeForever { list ->
            val data = dateFormat.format(Date())
            var isHave = false
            list!!.forEach {
                if (data.lowercase().contains(it.col_weekday!!.lowercase())) {
                    viewModel.getCusTomerByRegionId(it.col_reegionid)
                    isHave = true
                }
            }
            binding.emptyView.isVisible = !isHave
            viewModel.customerList.observeForever {
                listCustomers = it as ArrayList<Customer>
                adapter?.list = it
                binding.emptyView.isVisible = it.isEmpty()
            }
        }
        viewModel.getAllOders()
        viewModel.odderList.observeForever { list ->
            adapter?.orderList = list as ArrayList<OrderEntity>
            adapter?.notifyDataSetChanged()
            setProgress(list)
        }

        binding.searchEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter?.setFilter(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(list: ArrayList<OrderEntity>) {
        viewModel.customerList.observeForever { sList ->
            var progress = 0
            sList?.forEach {
                if (check(it.orgname, list)) {
                    progress++
                }
            }
            val completed = (progress * 100) / (adapter?.list?.size ?: 1)
            binding.progressView.progress = completed
            binding.progressTv.text = "Today Progress : ${progress}/ ${sList?.size} \t ( ${completed}% )"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        EventBus.getDefault().unregister(this)
    }

    companion object {
        val TAG = TodayFragment::class.java.name

        fun newInstance(isF: Boolean) = TodayFragment().apply {
            Bundle().apply {
                putBoolean("isFirst", isF)
            }
        }
    }

    @Subscribe
    fun onEvent(event: RefreshToday) {
        Log.e("Event", "$event")
        viewModel.getAllOders()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllOders()
    }

    fun check(name: String?, list: ArrayList<OrderEntity>): Boolean {
        var b = false
        list.forEach {
            if ((it.col_date ?: "").contains(simpleDateFormat.format(Date()), true)
                && (it.col_customername ?: "").lowercase()
                    .contains(name.toString().lowercase(), true)
            ) {
                b = true
            }
        }
        return b
    }


}