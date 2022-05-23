package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.databinding.ItemListBinding
import com.bsp.orderbooking.db.entity.Customer


class CusListAapter : RecyclerView.Adapter<CusListAapter.ViewHolder>() {

    var listFilter: ArrayList<Customer> = ArrayList()
    var list: ArrayList<Customer> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            listFilter = value
            notifyDataSetChanged()
        }

    class ViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFilter[position]
        holder.binding.itemName.text = item.orgname.toString() +" : "+ item.regionid
        holder.binding.description2Tv.text = item.address.toString()

    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilter(name: String) {
        val nList: ArrayList<Customer> = ArrayList()
        list.forEach {
            if (it.orgname?.lowercase()?.contains(name.lowercase(), true) == true ||
                it.address?.lowercase()?.contains(name.lowercase(), true) == true ||
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