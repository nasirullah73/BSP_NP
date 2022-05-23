package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.databinding.SingleItemLayoutBinding
import com.bsp.orderbooking.db.entity.ProductEntity
import com.bsp.orderbooking.util.ActivityUtils

class DropdownAapter(var onClickItem: OnClickItem,var alert: AlertDialog? = null) : RecyclerView.Adapter<DropdownAapter.ViewHolder>(){

    var list: ArrayList<ProductEntity> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(var binding: SingleItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SingleItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NewApi", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item = list[position]
        holder.binding.text1.text = item.col_productid.toString() + " - " + item.col_productname
        holder.binding.text2.text = "Price : "+item.col_retailprice +"  Size : "+ item.col_unitsize

        holder.itemView.setOnClickListener {
            if(alert != null) alert?.dismiss()
            onClickItem.onClick(item)
        }

    }

    override fun getItemCount(): Int {
      return list.size
    }

    interface OnClickItem{
        fun onClick(item : ProductEntity)
    }
}