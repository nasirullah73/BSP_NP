package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.databinding.ItemProductListBinding
import com.bsp.orderbooking.db.entity.ProductEntity

class ProductsListAapter : RecyclerView.Adapter<ProductsListAapter.ViewHolder>() {

    var listFilter: ArrayList<ProductEntity> = ArrayList()
    var list: ArrayList<ProductEntity> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            listFilter = value
            notifyDataSetChanged()
        }

    class ViewHolder(var binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NewApi", "SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFilter[position]
        holder.binding.itemName.text = item.col_productname.toString()
        holder.binding.description2Tv.text =
            "ID: " + item.col_productid + " , Price : " + item.col_retailprice

    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilter(name: String) {
        val nList: ArrayList<ProductEntity> = ArrayList()
        list.forEach {
            if (it.col_productname?.lowercase()?.contains(name.lowercase(), true) == true ||
                it.col_productid?.toString()?.lowercase()
                    ?.contains(name.lowercase(), true) == true ||
                name == ""
            ) {
                nList.add(it)
            }
        }
        listFilter = nList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearFilter() {
        listFilter = list
        notifyDataSetChanged()
    }

}