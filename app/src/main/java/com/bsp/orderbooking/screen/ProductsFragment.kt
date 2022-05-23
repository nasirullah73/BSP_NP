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
import com.bsp.orderbooking.adapter.ProductsListAapter
import com.bsp.orderbooking.databinding.ListLayoutBinding
import com.bsp.orderbooking.db.entity.ProductEntity
import com.bsp.orderbooking.events.RefreshProducts
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class ProductsFragment : Fragment() {

    var _binding: ListLayoutBinding? = null
    val binding get() = _binding!!

    val viewModel by viewModels<DataBaseViewModel>()
    var adapter = ProductsListAapter()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearLayout.isVisible = true
        binding.progressTv.isVisible = false
        binding.progressView.isVisible = false

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        viewModel.getAllProducts()
        viewModel.productsList.observeForever {
            adapter.list = it as ArrayList<ProductEntity>
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
        val TAG = ProductsFragment::class.java.name

        fun newInstance(isF: Boolean) = ProductsFragment().apply {
            Bundle().apply {
                putBoolean("isFirst", isF)
            }
        }
    }

    @Subscribe
    fun onEvent(event: RefreshProducts) {
        Log.e("Event", "${event::class.java.name}")
        viewModel.getAllProducts()
    }


}