package com.bsp.orderbooking.util

import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsp.orderbooking.R
import com.bsp.orderbooking.adapter.DropdownAapter
import com.bsp.orderbooking.db.entity.ProductEntity


object DialogsUtils {

    fun showDialog(activity: Activity ,item: DropdownAapter.OnClickItem, data: List<ProductEntity>){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        val customLayout: View = activity.layoutInflater.inflate(R.layout.dialog_layout, null)
        alertDialog.setView(customLayout)
        val alert: AlertDialog = alertDialog.create()
        alert.setCancelable(false)
        alert.setCanceledOnTouchOutside(false)
        val editText: EditText = customLayout.findViewById(R.id.userNAme_edittext)
        val submitBtn: Button = customLayout.findViewById(R.id.regbuttonmain)
        val listView: RecyclerView = customLayout.findViewById(R.id.recyclerview)
        val adapter = DropdownAapter(item,alert)
        listView.layoutManager  = LinearLayoutManager(activity)
        listView.adapter = adapter
        adapter.list = data as ArrayList<ProductEntity>
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val list : ArrayList<ProductEntity> = ArrayList()
                data.forEach {
                    if(
                        it.col_productname?.lowercase()!!.contains(s.toString().lowercase(),true) ||
                        it.col_productid.toString().lowercase().contains(s.toString().lowercase(),true)
                    ){
                        list.add(it)
                    }
                }
                adapter.list = list
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        submitBtn.setOnClickListener {
            alert.dismiss()
        }
        alert.show()
    }

}