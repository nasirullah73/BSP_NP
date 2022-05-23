package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bsp.orderbooking.R
import com.bsp.orderbooking.adapter.ProductsAapter
import com.bsp.orderbooking.databinding.ActivityAddProtuctsBinding
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.db.entity.ProductEntity
import com.bsp.orderbooking.db.entity.ProductSale
import com.bsp.orderbooking.events.RefreshToday
import com.bsp.orderbooking.util.ProgressDialogUtils
import com.bsp.orderbooking.util.UserData
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import org.greenrobot.eventbus.EventBus
import java.util.*


class AddProtuctsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddProtuctsBinding
    val viewModel by viewModels<DataBaseViewModel>()
    var proList: List<ProductEntity>? = null
    var productList: ArrayList<String> = ArrayList()
    var orderList: ArrayList<ProductSale> = ArrayList()
    var selectedItem: ProductEntity? = null
    var dialog: SweetAlertDialog? = null
    var totlQty: Int = 0
    var totalPrice: Int = 0
    var cid: String? = null
    var customer: String? = null
    lateinit var adapter: ProductsAapter

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables", "SetTextI18n", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProtuctsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        cid = intent.getStringExtra("cid")
        customer = intent.getStringExtra("cname")
        binding.topAppBar.title = customer
        binding.topAppBar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        adapter = ProductsAapter(object : ProductsAapter.ItemOnClick {
            override fun onClick(item: ProductSale) {
                orderList.remove(item)
                adapter.list = orderList
                totlQty -= item.col_qty ?: 0
                val p = selectedItem?.col_retailprice!! * item.col_qty!!
                totalPrice -= p.toInt()
                binding.textTotal.text = "Total QTY: $totlQty  & Total Price: $totalPrice"
                binding.totalLayout.isVisible = true
            }
        }, true)
        binding.proList.layoutManager = LinearLayoutManager(this)
        binding.proList.adapter = adapter

        viewModel.getAllProducts()
        var load = true
        viewModel.productsList.observeForever { list ->
            if (load) {
                val dialog = ProgressDialogUtils.createProgressDialog(this, "Loading..")
                dialog.show()
                proList = list as List<ProductEntity>
                productList.clear()
                proList?.forEach {
                    productList.add(it.col_productname.toString())
                }
                val adpater = ArrayAdapter(this, R.layout.text_item_layout, productList)
                binding.NameInput.threshold = 1
                binding.NameInput.setAdapter(adpater)
                load = false
                dialog.dismiss()
            }

        }
        viewModel.isLoading.observeForever {
            if (it) {
                dialog = ProgressDialogUtils.createProgressDialog(this)
                dialog!!.show()
            } else {
                if (dialog != null) dialog!!.dismiss()
            }
        }

        binding.cancel.setOnClickListener {
            binding.NameInput.setText("")
            binding.qtyInput.setText("")
        }
        binding.ok.setOnClickListener {

        }
        binding.buttonLogin.setOnClickListener {
            saveOrder()
        }

        binding.NameInput.setOnItemClickListener { _, _, _, _ ->
            binding.qtyInput.requestFocus()
        }

        binding.qtyInput.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (binding.NameInput.text!!.isEmpty()) {
                        binding.NameInput.error = "Select Product"
                        return false
                    }
                    binding.NameInput.error = null

                    if (binding.qtyInput.text!!.isEmpty()) {
                        binding.qtyInput.error = "QTY*"
                        return false
                    }
                    binding.qtyInput.error = null

                    binding.NameInput.text?.toString()?.let { s1 ->
                        viewModel.getProductByName(s1).also {
                            dialog =
                                ProgressDialogUtils.createProgressDialog(this@AddProtuctsActivity)
                            dialog!!.show()
                            selectedItem = it
                            val newItem = ProductSale(
                                _id = null,
                                col_bookingdate = UserData.simpleDateFormat.format(Date()),
                                col_uploaddate = UserData.simpleDateFormat.format(Date()),
                                col_customerid = cid.toString(),
                                col_porductid = selectedItem?.col_productid.toString(),
                                col_productname = selectedItem?.col_productname,
                                col_qty = binding.qtyInput.text.toString().toInt(),
                                col_retailprice = selectedItem?.col_retailprice,
                                col_status = "P",
                                col_userid = UserData.getUser(this@AddProtuctsActivity).id,
                                "", "", "",
                                "", "", "",
                                "c", "", "", ""
                            )
                            val q: Int = binding.qtyInput.text.toString().toInt()
                            totlQty += q
                            val p = selectedItem?.col_retailprice!! * q
                            totalPrice += p.toInt()
                            binding.textTotal.text =
                                "Total QTY: $totlQty  & Total Price: $totalPrice"
                            binding.totalLayout.isVisible = true
                            orderList.add(newItem)
                            adapter.list = orderList
                            binding.qtyInput.setText("")
                            binding.NameInput.setText("")
                            dialog!!.dismiss()
                            binding.NameInput.requestFocus()
                        }
                    }
                }
                return false
            }
        })


    }

    fun saveOrder() {
        val dialog = ProgressDialogUtils.createProgressDialog(this)
        dialog.show()
        if (orderList.isNotEmpty()) {
            viewModel.insertOrder(
                OrderEntity(
                    null,
                    UserData.simpleDateFormat.format(Date()),
                    totlQty.toString(),
                    totalPrice.toString(),
                    cid.toString(),
                    customer!!
                ), orderList
            )
            ProgressDialogUtils.showSuccess(dialog, "OK", "Save Order Successfully") {
                EventBus.getDefault().post(RefreshToday())
                finish()
            }
        } else {
            ProgressDialogUtils.showError(dialog, "Oops", "Empty Order Not Saved")
        }
    }

    fun keybord() {
        val view = this.currentFocus
        val mathod = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        mathod.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_products) {
            FrameActivity.startActivity(this, "Products List", ProductsFragment.TAG)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}