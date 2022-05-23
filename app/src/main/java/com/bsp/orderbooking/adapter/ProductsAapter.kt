package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ItemListBinding
import com.bsp.orderbooking.databinding.ProductItemListBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.ProductEntity
import com.bsp.orderbooking.db.entity.ProductSale
import com.bsp.orderbooking.db.entity.Schedule
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.Utilities

class ProductsAapter(var itemOnClick: ItemOnClick,var del : Boolean) : RecyclerView.Adapter<ProductsAapter.ViewHolder>(){


    var list: ArrayList<ProductSale> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selected: Int = -1
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(var binding: ProductItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NewApi", "SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item = list[position]
        holder.binding.itemName.text = item.col_productname.toString()
        holder.binding.description2Tv.text = "ID: "+item.col_porductid+"  Price : " + item.col_retailprice + "  QTY : "+item.col_qty
        if(del){
            holder.binding.imageViewDel.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_baseline_cancel_24_red))
        }else{
            holder.binding.imageViewDel.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_baseline_edit_24))
            if(selected == position){
                holder.binding.mainLayout.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.SteelBlue)
                )
            }else{
                holder.binding.mainLayout.setBackgroundColor(
                    holder.itemView.context.getColor(R.color.white)
                )
            }
        }

        holder.binding.imageViewDel.setOnClickListener {
            selected = position
            itemOnClick.onClick(item)
        }
    }

    override fun getItemCount(): Int {
      return list.size
    }
    
    interface ItemOnClick{
        fun onClick(item: ProductSale)
    }



}