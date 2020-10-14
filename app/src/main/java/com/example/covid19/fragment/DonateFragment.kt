package com.example.covid19.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid19.R
import com.example.covid19.helper.Functions
import com.example.covid19.ui.BaseActivity
import com.example.covid19.ui.DonationReceiptActivity
import kotlinx.android.synthetic.main.fragment_donate.*


class DonateFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as BaseActivity?
        actionListner()
    }

    fun actionListner() {
        btnDonateNext.setOnClickListener {
            if (edtDonateName.text.length == 0 || edtDonateName.text.isNullOrEmpty()) {
                edtDonateName.error = "Name is Required"
            } else if (edtDonateMobileNumber.text.length == 0 || edtDonateMobileNumber.text.isNullOrEmpty()) {
                edtDonateMobileNumber.error = "Mobile Number is Required"
            } else if (edtDonateMobileNumber.text.length < 10) {
                edtDonateMobileNumber.error = "Invalid Mobile Number"
            } else if (edtDonateAddress.text.length == 0 || edtDonateAddress.text.isNullOrEmpty()) {
                edtDonateAddress.error = "Address is Required"
            } else if (edtDonateEmail.text.length == 0 || edtDonateEmail.text.isNullOrEmpty()) {
                edtDonateEmail.error = "Email is Required"
            } else if (!(Functions.emailValidation(edtDonateEmail.text.trim().toString()))) {
                edtDonateEmail.error = "Invalid Email Id"
            } else if (edtDonateNationality.text.length == 0 || edtDonateNationality.text.isNullOrEmpty()) {
                edtDonateNationality.error = "Nationality is Required"
            } else if (edtDonateNationalityId.text.length == 0 || edtDonateNationalityId.text.isNullOrEmpty()) {
                edtDonateNationalityId.error = "Nationality ID is Required"
            } else if (edtDonateAmount.text.length == 0 || edtDonateAmount.text.isNullOrEmpty()) {
                edtDonateAmount.error = "Donation Amount is Required"
            } else {
                Log.d("TAG", "actionListner: "+"Call")
                DonationReceiptActivity.launchActivity(mActivity,edtDonateName.text.toString(),edtDonateMobileNumber.text.toString(),edtDonateAddress.text.toString(),edtDonateEmail.text.toString(),edtDonateNationality.text.toString(),edtDonateNationalityId.text.toString(),edtDonateAmount.text.toString())
            }

        }
    }

    companion object {
        fun getInstance(): DonateFragment {
            return DonateFragment()
        }

        var mActivity: BaseActivity? = null
    }
}