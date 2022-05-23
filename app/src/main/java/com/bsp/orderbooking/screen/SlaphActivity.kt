package com.bsp.orderbooking.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.bsp.orderbooking.R
import com.bsp.orderbooking.databinding.ActivitySlaphBinding
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.UserData

class SlaphActivity : AppCompatActivity() {

    lateinit var binding : ActivitySlaphBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlaphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = getColor(R.color.purple_700)
        proceed()

    }

    private fun proceed() {
        val secondsDelayed = 2
        Handler(Looper.getMainLooper()).postDelayed({
            if(permissioms()){
                moveNow()
            }
        }, (secondsDelayed * 1000).toLong())
    }

    private fun moveNow() {
        if(UserData.userLogIn(this)){
            ActivityUtils.startMainActivity(this,false)
            finish()
        }else{
            ActivityUtils.startLogInActivity(this)
            finish()
        }

    }

    @SuppressLint("NewApi")
    fun permissioms(): Boolean{
        return when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),123)
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        proceed()
    }


}