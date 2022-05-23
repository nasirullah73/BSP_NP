package com.bsp.orderbooking.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bsp.orderbooking.R
import com.bsp.orderbooking.database.DataBaseConnection
import com.bsp.orderbooking.databinding.ActivityLogInBinding
import com.bsp.orderbooking.models.ServerData
import com.bsp.orderbooking.models.UserInfo
import com.bsp.orderbooking.util.ActivityUtils
import com.bsp.orderbooking.util.App
import com.bsp.orderbooking.util.ProgressDialogUtils
import com.bsp.orderbooking.util.UserData
import com.bsp.orderbooking.viewModel.DataBaseViewModel
import kotlinx.coroutines.launch
import java.sql.Connection


class LogInActivity : AppCompatActivity() {

    lateinit var binding: ActivityLogInBinding
    var list: List<UserInfo?> = ArrayList()
    lateinit var dialog: SweetAlertDialog
    val viewModel by viewModels<DataBaseViewModel>()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        window.statusBarColor = getColor(R.color.purple_500)
        //getData()
        binding.buttonLogin.setOnClickListener {
            validation()
        }
    }

    fun validation() {
        if (binding.userNAmeEdittext.text?.isEmpty() == true) {
            binding.userNameInput.error = "Enter User ID"
            return
        }
        binding.userNameInput.error = null

        if (binding.passwordEdittext.text?.isEmpty() == true) {
            binding.passwordLayout.error = "Enter Password"
            return
        }
        binding.passwordLayout.error = null
        checkUser()
    }

    fun checkUser() {
        dialog = ProgressDialogUtils.createProgressDialog(this)
        dialog.show()
        if (list.isEmpty()) {
            ProgressDialogUtils.showError(
                dialog, "oops", "No Data..! Please Sync Data"
            )
        } else {
            list.forEach {
                if (it?.id?.toString()?.lowercase() == binding.userNAmeEdittext.text.toString()
                        .lowercase()
                    && it.password == binding.passwordEdittext.text.toString()
                ) {
                    if (userInfro()) {
                        UserData.clearAll(this)
                        viewModel.clearAll()
                    }
                    dialog.dismiss()
                    UserData.saveUser(
                        this,
                        it.name, it.password!!, it.id!!.toInt(), it.comp!!.toInt()
                    )
                    ActivityUtils.startMainActivity(this, userInfro())
                    finish()
                } else {
                    ProgressDialogUtils.showError(
                        dialog, "oops", "Wrong User Name & Password"
                    )
                }
            }
        }
    }

    fun userInfro(): Boolean {
        val editor = UserData.getSharedPreferences(this)
        val user = editor.getString(UserData.USER_NAME, "")
        if (binding.userNAmeEdittext.text.toString() == user.toString()) {
            return false
        }
        return true
    }

    fun getData() {
        dialog = ProgressDialogUtils.createProgressDialog(this)
        dialog.show()
        object : DataBaseConnection() {
            override fun onPostExecute(connection: Connection) {
                lifecycleScope.launch {
                    list = getUsers(connection)
                    dialog.dismissWithAnimation()
                }
            }

            override fun onError(connection: String) {
                ProgressDialogUtils.showError(dialog, "SQL Error", connection)
            }
        }.execute(UserData.getIP(App.context))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sync -> {
                getData()
                true
            }
            R.id.action_sever -> {
                ProgressDialogUtils.dialodForIPAddress(this@LogInActivity,
                    object : ProgressDialogUtils.DialogSubmitClick {
                        override fun onClick(ip: ServerData) {
                            UserData.saveIP(this@LogInActivity, ip)
                        }
                    })
                true
            }
            else -> false
        }
    }

}