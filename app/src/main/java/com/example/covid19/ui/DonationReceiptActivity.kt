package com.example.covid19.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import com.example.covid19.R
import com.example.covid19.helper.Functions
import kotlinx.android.synthetic.main.activity_donation_receipt.*
import kotlinx.android.synthetic.main.toolbar_activity.*

class DonationReceiptActivity : BaseActivity() {
    private var mLastClickTime: Long = 0

    companion object {
        fun launchActivity(activity: BaseActivity?,name : String,mobileNumber : String, address : String , email : String , nationality : String , nationalityId : String , Amount : String) {
            if (activity != null) {
                var intent = Intent(activity,DonationReceiptActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("mobileNumber",mobileNumber)
                intent.putExtra("address",address)
                intent.putExtra("email",email)
                intent.putExtra("nationality",nationality)
                intent.putExtra("nationalityId",nationalityId)
                intent.putExtra("Amount",Amount)
                Functions.fireIntent(activity, intent, true, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_receipt)
        initViews()
        img_back.setOnClickListener {
            Functions.hideKeyPad(this, it)
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            onBackPressed()
        }
    }
    fun initViews()
    {
        txtName.setText(intent.getStringExtra("name").toString())
        txtMobileNumber.setText(intent.getStringExtra("mobileNumber").toString())
        txtAddress.setText(intent.getStringExtra("address").toString())
        txtEmail.setText(intent.getStringExtra("email").toString())
        txtNationality.setText(intent.getStringExtra("nationality").toString())
        txtNationalityId.setText(intent.getStringExtra("nationalityId").toString())
        txtAmount.setText(intent.getStringExtra("Amount").toString())
    }
    override fun onBackPressed() {
        super.onBackPressed()
        Functions.fireIntent(this, false)
    }
}