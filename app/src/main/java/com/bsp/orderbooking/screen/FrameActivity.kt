package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ActivityFrameBinding
import com.bsp.orderbooking.util.ActivityUtils

class FrameActivity : AppCompatActivity() {
    lateinit var binding: ActivityFrameBinding

    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        val title = intent.getStringExtra(KEY_TITLE)
        binding.topAppBar.title = title
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        binding.topAppBar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        supportFragmentManager.beginTransaction().replace(
            binding.frame.id,
            ActivityUtils.initFragment(
                supportFragmentManager.fragmentFactory,
                intent.getStringExtra(KEY_FRAGMENT) ?: "",
                intent.extras
            )
        ).commit()

    }

    companion object {
        val KEY_FRAGMENT = "keyFragment"
        val KEY_TITLE = "keyTitle"

        fun startActivity(
            context: Context,
            title: String,
            fragment: String,
            bundle: Bundle? = null
        ) {
            val intent = Intent(context, FrameActivity::class.java).apply {
                putExtra(KEY_TITLE, title)
                putExtra(KEY_FRAGMENT, fragment)
                bundle?.let { putExtras(it) }
            }
            ActivityUtils.startActivity(context, intent)

        }
    }
}