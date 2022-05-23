package com.bsp.orderbooking.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bsp.orderbooking.MainActivity
import com.bsp.orderbooking.screen.*

object ActivityUtils {

    fun startActivity(context: Context, intent: Intent) {
        context.startActivity(intent)
    }

    fun startMainActivity(context: Context, isF: Boolean) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("isFirst", isF)
        startActivity(context, intent)
    }

    fun startLogInActivity(context: Context) {
        startActivity(context, Intent(context, LogInActivity::class.java))
    }

    fun startVisitsPlanActivity(context: Context) {
        startActivity(context, Intent(context, VisitsPlanActivity::class.java))
    }

    fun startSyncActivity(context: Context) {
        startActivity(context, Intent(context, SyncActivity::class.java))
    }

    fun startCustomersActivity(context: Context, id: Int, isF: Boolean) {
        val intent = Intent(context, CustomersActivity::class.java)
        intent.putExtra("rid", id)
        intent.putExtra("isFirst", isF)
        startActivity(context, intent)
    }

    fun startAddProtuctsActivity(context: Context, id: String, name: String) {
        val intent = Intent(context, AddProtuctsActivity::class.java)
        intent.putExtra("cid", id)
        intent.putExtra("cname", name)
        startActivity(context, intent)
    }


    fun initFragment(factory: FragmentFactory, tag: String, bundle: Bundle? = null): Fragment {
        val fregment = factory.instantiate(ClassLoader.getSystemClassLoader(),tag)
        bundle?.let { fregment.arguments= it }
        return fregment
    }

    fun startOrderDetailActivity(
        context: Context,
        id: String,
        qty: String,
        price: String,
        date: String,
        cid: String,
        name: String
    ) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra("ordid", id)
        intent.putExtra("ordqty", qty)
        intent.putExtra("ordprice", price)
        intent.putExtra("orddate", date)
        intent.putExtra("ordcid", cid)
        intent.putExtra("ordcname", name)
        startActivity(context, intent)
    }

    fun startOrderListActivity(context: Context) {
        startActivity(context, Intent(context, OrderListActivity::class.java))
    }
}