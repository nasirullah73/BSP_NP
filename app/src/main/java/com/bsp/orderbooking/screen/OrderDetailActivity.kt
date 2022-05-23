package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.LabeledIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsp.orderbooking.R
import com.bsp.orderbooking.adapter.DropdownAapter
import com.bsp.orderbooking.adapter.ProductsAapter
import com.bsp.orderbooking.databinding.ActivityOrderDetailBinding
import com.bsp.orderbooking.db.entity.OrderEntity
import com.bsp.orderbooking.db.entity.ProductEntity
import com.bsp.orderbooking.db.entity.ProductSale
import com.bsp.orderbooking.util.DialogsUtils
import com.bsp.orderbooking.util.UserData
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityOrderDetailBinding
    val viewModel by viewModels<DataBaseViewModel>()
    var selectedProduct: ProductSale? = null
    var proList: List<ProductEntity>? = null
    var oldeQty = 0
    var oldePrice = 0.0
    var rid: String? = ""
    var rdate: String? = ""
    var rqrt: String? = ""
    var rprice: String? = ""
    var cid: String? = ""
    var cname: String? = ""
    var update = true
    var adapter: ProductsAapter? = null
    var isEnable = true

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables", "SetTextI18n", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        rid = intent.getStringExtra("ordid")
        rdate = intent.getStringExtra("orddate")
        rqrt = intent.getStringExtra("ordqty")
        rprice = intent.getStringExtra("ordprice")
        cid = intent.getStringExtra("ordcid")
        cname = intent.getStringExtra("ordcname")
        binding.topAppBar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        adapter = ProductsAapter(
            object : ProductsAapter.ItemOnClick {
                override fun onClick(item: ProductSale) {
                    if (item.col_status == "P") {
                        binding.editform.isVisible = true
                        binding.addBtn.isVisible = false
                        selectedProduct = item
                        oldeQty = item.col_qty!!
                        oldePrice = item.col_retailprice!!
                        binding.NameInput.setText(item.col_productname)
                        binding.qtyInput.setText(item.col_qty.toString())
                    }
                }
            }, false
        )

        viewModel.getAllProducts()
        viewModel.productsList.observeForever {
            proList = it as List<ProductEntity>
        }

        binding.delete.setOnClickListener {
            if (selectedProduct != null) {
                viewModel.deleteProductSale(selectedProduct?._id!!)
                rqrt = (rqrt!!.toString().toInt() - selectedProduct?.col_qty!!).toString()
                rprice = (rprice.toString()
                    .toDouble() - (selectedProduct!!.col_qty!! * selectedProduct!!.col_retailprice!!)).toString()

                viewModel.updateOrder(
                    OrderEntity(
                        rid!!.toInt(), rdate, rqrt, rprice, cid, cname
                    )
                )
                binding.description2Tv.text = "Customer : ${cname}\nPrice: ${rprice}, Qty:${rqrt}"
                viewModel.orderDetailbyid(rid!!)
            }
            binding.editform.isVisible = false
            selectedProduct = null
            oldeQty = 0
            oldePrice = 0.0
            binding.NameInput.setText("")
            binding.qtyInput.setText("")
            binding.NameInput.isFocusable = false
            binding.addBtn.isVisible = true
            adapter?.selected = -1
        }

        binding.addBtn.setOnClickListener {
            if (isEnable) {
                binding.editform.isVisible = true
                binding.addBtn.isVisible = false
                adapter?.selected = -1
            }
        }

        binding.cancel.setOnClickListener {
            binding.editform.isVisible = false
            selectedProduct = null
            oldeQty = 0
            oldePrice = 0.0
            binding.NameInput.setText("")
            binding.qtyInput.setText("")
            binding.NameInput.isFocusable = false
            binding.addBtn.isVisible = true
            adapter?.selected = -1
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
        binding.itemName.text = "Order ID: $rid"
        binding.description1Tv.text = "Order Date: $rdate"
        binding.description2Tv.text = "Customer : ${cname}\nPrice: ${rprice}, Qty:${rqrt}"
        viewModel.orderDetailbyid(rid!!)
        viewModel.orderDetailList.observeForever {
            adapter?.list = it as ArrayList<ProductSale>
            isEnable = it[0].col_status == "P"
        }

        binding.NameInput.setOnClickListener {
            if (selectedProduct == null) {
                DialogsUtils.showDialog(this, object : DropdownAapter.OnClickItem {
                    override fun onClick(item: ProductEntity) {
                        update = false
                        binding.NameInput.setText(item.col_productname)
                        selectedProduct = ProductSale(
                            null,
                            UserData.simpleDateFormat2.format(Date()),
                            UserData.simpleDateFormat2.format(Date()),
                            cid.toString(),
                            item.col_productid.toString(),
                            item.col_productname,
                            0,
                            item.col_retailprice,
                            "P",
                            UserData.getUser(this@OrderDetailActivity).id,
                            "", "", "",
                            "", "", "",
                            "c", "", "", rid
                        )
                        binding.qtyInput.requestFocus()
                        keybord()
                    }
                }, proList as List<ProductEntity>)
            }
        }

        binding.ok.setOnClickListener {
            if (binding.qtyInput.text!!.isEmpty()) {
                binding.qtyInput.error = "QTY*"
                return@setOnClickListener
            }
            binding.qtyInput.error = null
            if (selectedProduct == null) return@setOnClickListener
            val q = binding.qtyInput.text.toString().toInt()
            selectedProduct?.col_qty = q
            if (update) {
                viewModel.updateProductSale(selectedProduct!!)
                rqrt = ((rqrt!!.toString().toInt() - oldeQty) + q).toString()
                rprice = ((rprice.toString()
                    .toDouble() - oldePrice) + (selectedProduct!!.col_qty!! * selectedProduct!!.col_retailprice!!)).toString()

            } else {
                viewModel.insertProductSale(selectedProduct!!)
                rqrt = (rqrt!!.toString().toInt() + q).toString()
                rprice = (rprice.toString()
                    .toDouble() + (selectedProduct!!.col_qty!! * selectedProduct!!.col_retailprice!!)).toString()
            }

            viewModel.updateOrder(
                OrderEntity(
                    rid!!.toInt(), rdate, rqrt, rprice, cid, cname
                )
            )

            binding.description2Tv.text = "Customer : ${cname}\nPrice: ${rprice}, Qty:${rqrt}"
            binding.editform.isVisible = false
            binding.addBtn.isVisible = true
            selectedProduct = null
            oldeQty = 0
            oldePrice = 0.0
            binding.NameInput.setText("")
            binding.qtyInput.setText("")
            viewModel.orderDetailbyid(rid!!)
            binding.NameInput.isFocusable = false
            adapter?.selected = -1
        }
    }

    fun keybord() {
        val view = this.currentFocus
        val mathod = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        mathod.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    @SuppressLint("NewApi", "QueryPermissionsNeeded")
    fun sentOrder() {
        var msg = "Order Detail\n" +
                "Order Date: $rdate \n" +
                "Customer : $cid - $cname \n" +
                "Products List :\n" +
                "_____________________\n"

        adapter?.list?.forEachIndexed { index, productSale ->
            msg += "${index + 1})\n" +
                    "Product ID: ${productSale.col_porductid}\n" +
                    "Product Name: ${productSale.col_productname}\n" +
                    "Qty: ${productSale.col_qty} \n" +
                    "_____________________\n"
        }

        msg += "Total Products : ${adapter?.list?.size}"


        val emailIntent = Intent()
        emailIntent.action = Intent.ACTION_SEND
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY)
        )
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT, cname ?: "Customer"
        )
        emailIntent.type = "message/rfc822"

        val pm = packageManager
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"


        val openInChooser =
            Intent.createChooser(emailIntent, "Share Via")

        val resInfo = pm.queryIntentActivities(sendIntent, 0)
        val intentList: MutableList<LabeledIntent> = ArrayList()
        for (i in resInfo.indices) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            val ri = resInfo[i]
            val packageName = ri.activityInfo.packageName
            Log.e("Intents:", packageName)
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName)
            } else if (packageName.contains("twitter")
                || packageName.contains("facebook")
                || packageName.contains("mms")
                || packageName.contains("android.gm")
                || packageName.contains("whatsapp")
                || packageName.contains("messaging")
            ) {
                val intent = Intent()
                intent.component = ComponentName(packageName, ri.activityInfo.name)
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                when {
                    packageName.contains("twitter") -> {
                        intent.putExtra(Intent.EXTRA_TEXT, msg)
                    }
                    packageName.contains("whatsapp") -> {
                        intent.putExtra(Intent.EXTRA_TEXT, msg)
                    }
                    packageName.contains("messaging") -> {
                        intent.type = "vnd.android.dir/mms/sms"
                        intent.putExtra("sms_body", msg)
                    }
                    packageName.contains("facebook") -> {
                        intent.putExtra(Intent.EXTRA_TEXT, msg)
                    }
                    packageName.contains("mms") -> {
                        intent.putExtra(Intent.EXTRA_TEXT, msg)
                    }
                    packageName.contains("android.gm") -> {
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY)
                        )
                        intent.putExtra(
                            Intent.EXTRA_SUBJECT, cname ?: "Customer"
                        )
                        intent.type = "message/rfc822"
                    }
                }
                intentList.add(LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon))
            }
        }
        val extraIntents = intentList.toTypedArray()

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents)
        startActivity(openInChooser)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sent -> {
                sentOrder()
                true
            }
            else -> false
        }
    }
}