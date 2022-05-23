package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.adapter.CustomersListAapter
import com.bsp.orderbooking.databinding.ListLayoutBinding
import com.bsp.orderbooking.db.entity.Schedule
import com.bsp.orderbooking.events.RefreshCustomers
import com.bsp.orderbooking.events.RefreshSchdule
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class VistPlanFragment : Fragment() {

    var _binding: ListLayoutBinding? = null
    val binding get() = _binding!!

    val viewModel by viewModels<DataBaseViewModel>()
    var adapter = CustomersListAapter()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("EEEE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter

        viewModel.schduleList.observeForever {
            adapter.list = it as ArrayList<Schedule>
            binding.emptyView.isVisible = it.isEmpty()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        EventBus.getDefault().unregister(this)
    }

    companion object {
        val TAG = VistPlanFragment::class.java.name

        fun newInstance(isF: Boolean) = VistPlanFragment().apply {
            Bundle().apply {
                putBoolean("isFirst", isF)
            }
        }
    }

    @Subscribe
    fun onEvent(event: RefreshSchdule) {
        viewModel.loadSchdule()
    }


}