package com.bsp.orderbooking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ItemListBinding
import com.bsp.orderbooking.db.entity.Customer
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.util.ActivityUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomersAapter(var isEnabel: Boolean) : RecyclerView.Adapter<CustomersAapter.ViewHolder>() {

    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    var listFilter: ArrayList<Customer> = ArrayList()
    var list: ArrayList<Customer> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            listFilter = value
            notifyDataSetChanged()
        }


    var orderList: ArrayList<OrderEntity> = ArrayList()
    /* set(value) {
         field = value
     }*/

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

    @SuppressLint("NewApi", "SetTextI18n", "UseCompatLoadingForDrawables", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFilter[position]
        val context = holder.itemView.context
        holder.binding.itemName.text = item.orgname ?: ""
        holder.binding.description2Tv.text = item.address
        holder.binding.descriptionTv.isVisible = false
        holder.binding.imageView.isVisible = false

        holder.itemView.setOnClickListener {
            if (!check(item.orgname) && isEnabel) {
                ActivityUtils.startAddProtuctsActivity(
                    context,
                    item.customerid!!,
                    item.orgname!!
                )
            } else if (isEnabel) {
                val order = getOrder(item.orgname)
                ActivityUtils.startOrderDetailActivity(
                    context,
                    order?._id.toString(),
                    order?.col_qty.toString(),
                    order?.col_price.toString(),
                    order?.col_date.toString(),
                    order?.col_customerid.toString(),
                    order?.col_customername.toString()
                )
            }
        }
        holder.binding.statusView.isVisible = false
        holder.binding.status.isVisible = isEnabel
        if (isEnabel) {
            val order = getOrder(item.orgname)
            if (order != null) {
                val date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(order.col_date)
                holder.binding.status.isVisible = false
                holder.binding.descriptionTv.isVisible = true
                holder.binding.descriptionTv.text =
                    "Order Time ${simpleTimeFormat.format(date)} , Value: ${order.col_price}"
                holder.binding.status.background =
                    context.getDrawable(R.drawable.custom_circle_green)
                holder.binding.itemName.setTextColor(context.getColor(R.color.white))
                holder.binding.descriptionTv.setTextColor(context.getColor(R.color.white))
                holder.binding.description2Tv.setTextColor(context.getColor(R.color.white))
                holder.itemView.setBackgroundColor(context.getColor(R.color.DimGray))
                holder.binding.itemImage.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_portrait_green))
            } else {
                holder.binding.status.isVisible = true
                holder.binding.descriptionTv.isVisible = false
                holder.binding.itemName.setTextColor(context.getColor(R.color.black))
                holder.binding.descriptionTv.setTextColor(context.getColor(R.color.black))
                holder.binding.description2Tv.setTextColor(context.getColor(R.color.black))
                holder.binding.status.background =
                    context.getDrawable(R.drawable.custom_circle)
                holder.itemView.setBackgroundColor(context.getColor(R.color.white))
                holder.binding.itemImage.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_portrait_24))
            }
        }

    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    fun check(name: String?): Boolean {
        var b = false
        orderList.forEach {
            if ((it.col_date ?: "").contains(simpleDateFormat.format(Date()), true)
                && (it.col_customername ?: "").lowercase()
                    .contains(name.toString().lowercase(), true)
            ) {
                b = true
            }
        }
        return b
    }

    fun getOrder(name: String?): OrderEntity? {
        var b: OrderEntity? = null
        orderList.forEach {
            if ((it.col_date ?: "").contains(simpleDateFormat.format(Date()), true)
                && (it.col_customername ?: "").lowercase()
                    .contains(name.toString().lowercase(), true)
            ) {
                b = it
            }
        }
        return b
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