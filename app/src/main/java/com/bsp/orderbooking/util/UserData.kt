package com.bsp.orderbooking.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.bsp.orderbooking.database.DataBaseConnection
import com.bsp.orderbooking.models.ServerData
import com.bsp.orderbooking.models.UserInfo
import java.text.SimpleDateFormat
import java.util.*

object UserData {

    val SHARE_PREFRENCE_NAME = "currantUserDate"
    val USER_NAME = "userName"
    val USER_PASSWORD = "userPassword"
    val USER_IMAGE = "userImage"
    val USER_EMAIL = "userEmail"
    val USER_DATE = "userDate"
    val USER_LOGIN = "userLogIn"
    val SERVER_IP = "serverIP"
    val SERVER_DATABASE = "serverDatabase"
    val SERVER_USER = "serverUser"
    val SERVER_PASSWORD = "serverPassword"

    @SuppressLint("ConstantLocale")
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val simpleDateFormat2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun saveUser(context: Context, name: String?, password: String, image: Int, comp: Int) {
        val editor = getSharedPreferencesEditor(context)
        editor.putString(USER_NAME, name)
        editor.putString(USER_PASSWORD, password)
        editor.putInt(USER_IMAGE, image)
        editor.putInt(USER_DATE, comp)
        editor.putBoolean(USER_LOGIN, true)
        editor.commit()
    }

    fun saveIP(context: Context, ip: ServerData) {
        val editor = getSharedPreferencesEditor(context)
        editor.putString(SERVER_IP, ip.ip)
        editor.putString(SERVER_DATABASE, ip.database)
        editor.putString(SERVER_USER, ip.user)
        editor.putString(SERVER_PASSWORD, ip.password)
        editor.commit()
    }

    fun getIP(context: Context): ServerData {
        val editor = getSharedPreferences(context)
        return ServerData(
            editor.getString(SERVER_IP, DataBaseConnection.IP_ADDRESS)
                ?: DataBaseConnection.IP_ADDRESS,
            editor.getString(SERVER_DATABASE, DataBaseConnection.DATABASE_NAME)
                ?: DataBaseConnection.DATABASE_NAME,
            editor.getString(SERVER_USER, DataBaseConnection.USER)
                ?: DataBaseConnection.USER,
            editor.getString(SERVER_PASSWORD, DataBaseConnection.PASSWORD)
                ?: DataBaseConnection.PASSWORD
        )
    }

    fun getUser(context: Context): UserInfo {
        val editor = getSharedPreferences(context)
        return UserInfo(
            editor.getString(USER_NAME, ""),
            editor.getString(USER_PASSWORD, ""),
            editor.getInt(USER_IMAGE, 0),
            editor.getInt(USER_DATE, 0)
        )
    }

    fun userLogIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(USER_LOGIN, false)
    }

    fun clearAll(context: Context) {
        getSharedPreferencesEditor(context).clear()
    }

    fun deleteUser(context: Context) {
        getSharedPreferencesEditor(context).putBoolean(USER_LOGIN, false).commit()
    }


    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARE_PREFRENCE_NAME, MODE_PRIVATE)
    }

    fun getSharedPreferencesEditor(context: Context): SharedPreferences.Editor {
        return getSharedPreferences(context).edit()
    }


}