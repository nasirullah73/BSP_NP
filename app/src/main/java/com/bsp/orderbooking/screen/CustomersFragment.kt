package com.bsp.orderbooking.screen

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
import com.bsp.orderbooking.adapter.CusListAapter
import com.bsp.orderbooking.databinding.ListLayoutBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.events.RefreshCustomers
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.collections.ArrayList

class CustomersFragment : Fragment() {

    var _binding: ListLayoutBinding? = null
    val binding get() = _binding as ListLayoutBinding
    val viewModel by viewModels<DataBaseViewModel>()
    var adapter = CusListAapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearLayout.isVisible = true
        binding.progressTv.isVisible = false
        binding.progressView.isVisible = false
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        viewModel.loadCustomers()
        viewModel.customerList.observeForever {
            adapter.list = it as ArrayList<Customer>
            binding.emptyView.isVisible = it.isEmpty()
        }
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.setFilter(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        EventBus.getDefault().unregister(this)
    }

    companion object {
        val TAG: String = CustomersFragment::class.java.name

        fun newInstance(isF: Boolean) = CustomersFragment().apply {
            Bundle().apply {
                putBoolean("isFirst", isF)
            }
        }
    }

    @Subscribe
    fun onEvent(event: RefreshCustomers) {
        Log.e("Event", "${event::class.java.name}")
        viewModel.loadCustomers()
    }


}