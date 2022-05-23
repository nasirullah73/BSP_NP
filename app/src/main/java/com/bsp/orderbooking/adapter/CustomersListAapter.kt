package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ItemListBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.Schedule
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.Utilities

class CustomersListAapter : RecyclerView.Adapter<CustomersListAapter.ViewHolder>(){

    var list: ArrayList<Schedule> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
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

    @SuppressLint("NewApi", "SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item = list[position]
        holder.binding.itemName.text = Utilities.dayName(item.col_weekday ?: "")
        holder.binding.description2Tv.text = item.col_regiondesc
        holder.binding.descriptionTv.isVisible = true
        holder.binding.imageView.isVisible = false

        holder.binding.itemImage.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.van_icon))

        holder.itemView.setOnClickListener {
            ActivityUtils.startCustomersActivity(
                holder.itemView.context,item.col_reegionid?: 0,false
            )
        }

    }

    override fun getItemCount(): Int {
      return list.size
    }
}