package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ItemListBinding
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.Utilities

class OrderListAapter : RecyclerView.Adapter<OrderListAapter.ViewHolder>(){

    var list: ArrayList<OrderEntity> = ArrayList()
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
        holder.binding.itemName.text = "${item.col_date} - ID: ${item._id}"
        holder.binding.description2Tv.text = "Customer: ${item.col_customerid}-${item.col_customername}\nPrice: ${item.col_price}, Qty:${item.col_qty}"
        holder.binding.descriptionTv.isVisible = false
        holder.binding.imageView.isVisible = false
        holder.binding.itemImage.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_orders))

        holder.itemView.setOnClickListener {
            ActivityUtils.startOrderDetailActivity(holder.itemView.context
                ,item._id.toString()
                ,item.col_qty.toString()
                ,item.col_price.toString()
                ,item.col_date.toString()
                ,item.col_customerid.toString()
                ,item.col_customername.toString()
            )
        }

    }

    override fun getItemCount(): Int {
      return list.size
    }
}