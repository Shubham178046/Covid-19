package com.example.covid19.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.covid19.R
import com.example.covid19.helper.Functions
import com.example.covid19.helper.MDToast
import com.example.covid19.helper.PrefUtil
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {
    companion object {
        fun launchActivity(activity: BaseActivity?) {
            if (activity != null) {
                val intent = Intent(activity, LoginActivity::class.java)
                Functions.fireIntent(activity, LoginActivity::class.java, true, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(this.resources.getColor(R.color.status_color))
        }
        actionListner()
    }

    fun actionListner() {
        btnLogin.setOnClickListener {
            if (edtMobileNumber.text.length == 0 || edtMobileNumber.text.isEmpty()) {
                edtMobileNumber.error = "Mobile Number Is Required"
            }
            /*else if(edtMobileNumber.text.length < 10)
            {
                edtMobileNumber.error = "Invalid Mobile Number"
            }*/
            else {
                /* if (edtMobileNumber.text.trim().toString().equals("8849071403")) {
                     PrefUtil.setLoggedIn(this@LoginActivity, true)
                     Functions.showToast(this, "Login Successfull", MDToast.TYPE_SUCCESS)
                     MainActivity.launchActivity(this)
                 } else {
                     Functions.showToast(this, "Invalid Login", MDToast.TYPE_ERROR)
                 }*/
                OTPVerificationActivity.launchActivity(this, edtMobileNumber.text.toString())
            }
        }
    }
}