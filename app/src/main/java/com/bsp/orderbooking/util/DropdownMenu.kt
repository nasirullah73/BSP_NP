package com.bsp.orderbooking.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.adapter.DropdownAapter
import com.bsp.orderbooking.databinding.DropmanuBinding
import com.bsp.orderbooking.db.entity.ProductEntity

class DropdownMenu(private val context: Context, val data: List<ProductEntity>? = null,categorySelectedListener: DropdownAapter.OnClickItem) :
    PopupWindow(
        context
    ) {

    private var dropdownAdapter: DropdownAapter = DropdownAapter(categorySelectedListener)
    var binding: DropmanuBinding? = null


    fun setList(list : List<ProductEntity>){
        dropdownAdapter.list = list as ArrayList<ProductEntity>
    }

    private fun setupView() {
        binding = DropmanuBinding.inflate(LayoutInflater.from(context))


        binding?.recyclerview?.setHasFixedSize(true)
        binding?.recyclerview?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.recyclerview?.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        data?.let {
            dropdownAdapter.list = it as ArrayList<ProductEntity>
        }
        binding?.recyclerview?.adapter = dropdownAdapter
        contentView = binding?.root
    }

    init {
        setupView()
    }

    companion object {
        fun showMenu(
            view: View,
            item: DropdownAapter.OnClickItem,
            data: List<ProductEntity>
        ) {
            Utilities.hideKeyboard(view)
            val menu = DropdownMenu(view.context, data,item)
            menu.setBackgroundDrawable(
                ActivityCompat.getDrawable(view.context, android.R.color.transparent)
            )
            menu.height = WindowManager.LayoutParams.WRAP_CONTENT
            menu.width = view.width
            menu.isOutsideTouchable = true
            menu.isFocusable = true
            menu.showAsDropDown(view)
        }
    }
}
