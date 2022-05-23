package com.bsp.orderbooking.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.LinearLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bsp.orderbooking.R
import com.bsp.orderbooking.database.DataBaseConnection
import com.bsp.orderbooking.models.ServerData

object ProgressDialogUtils {


    @SuppressLint("NewApi")
    fun createProgressDialog(
        context: Context,
        msg: String? = null,
        cancalable: Boolean? = false
    ): SweetAlertDialog {
        // context.theme.applyStyle(R.style.myAlertDialogTheme, true)
        val mProgressDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(cancalable == true)
        mProgressDialog.titleText = msg ?: context.getString(R.string.loading)
        mProgressDialog.progressHelper.circleRadius = 80
        mProgressDialog.progressHelper.barColor = context.getColor(R.color.purple_700)
        return mProgressDialog
    }

    @SuppressLint("NewApi")
    fun showProgress(
        dialog: SweetAlertDialog,
        title: String? = null,
        message: String? = null
    ): SweetAlertDialog {
        dialog.titleText = title ?: dialog.context.getString(R.string.loading)
        dialog.contentText = message
        dialog.confirmText = dialog.context.getString(R.string.ok)
        dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
        dialog.progressHelper.circleRadius = 80
        dialog.progressHelper.barColor = App.context.getColor(R.color.purple_700)
        return dialog
    }

    fun showError(dialog: SweetAlertDialog, title: String?, message: String?) {
        dialog.titleText = title
        dialog.contentText = message
        dialog.confirmText = dialog.context.getString(R.string.ok)
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
    }

    fun showSuccess(
        dialog: SweetAlertDialog,
        title: String?,
        message: String?,
        okListener: SweetAlertDialog.OnSweetClickListener? = null
    ) {
        dialog.setCancelable(false)
        dialog.titleText = title
        dialog.contentText = message
        dialog.confirmText = dialog.context.getString(R.string.ok)
        dialog.setConfirmClickListener(okListener)
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
    }

    fun dialodForIPAddress(context: Context, onSubmit: DialogSubmitClick) {
        val data = UserData.getIP(context)
        val layoutView = LinearLayout(context)
        layoutView.orientation = LinearLayout.VERTICAL
        val editTextIp = EditText(context)
        editTextIp.hint = "IP Address"
        editTextIp.setText(data.ip)
        val editTextDatabase = EditText(context)
        editTextDatabase.hint = "Database Name"
        editTextDatabase.setText(data.database)
        val editTextUser = EditText(context)
        editTextUser.hint = "Database User"
        editTextUser.setText(data.user)
        val editTextPassword = EditText(context)
        editTextPassword.hint = "User Password"
        editTextPassword.setText(data.password)
        editTextPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        layoutView.addView(editTextIp)
        layoutView.addView(editTextDatabase)
        layoutView.addView(editTextUser)
        layoutView.addView(editTextPassword)

        val mProgressDialog = SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
        mProgressDialog.setCustomView(layoutView)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(false)
        mProgressDialog.titleText = " Server Setting"
        mProgressDialog.setConfirmButton("Submit") {
            mProgressDialog.dismissWithAnimation()
            onSubmit.onClick(
                ServerData(
                    editTextIp.text.toString(),
                    editTextDatabase.text.toString(),
                    editTextUser.text.toString(),
                    editTextPassword.text.toString()
                )
            )
        }
        mProgressDialog.show()

    }

    interface DialogSubmitClick {
        fun onClick(ip: ServerData)
    }


}