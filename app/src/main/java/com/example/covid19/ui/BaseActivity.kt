package com.example.covid19.ui

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.covid19.R
import com.example.covid19.fragment.*
import kotlinx.android.synthetic.main.toolbar_menu.*
import java.util.*

open class BaseActivity : AppCompatActivity() {
    var selectedScreen = 0
    var fragmentBackStack: Stack<Fragment>? = null
    var showBackMessage: Boolean? = true
    var dialog: ProgressDialog? = null
    var customdialog: Dialog? = null

    fun setShowBackMessage(showBackMessage: Boolean) {
        this.showBackMessage = showBackMessage
    }

    fun getFragments(): Stack<Fragment>? {
        return fragmentBackStack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        fragmentBackStack = Stack()
    }

    fun doubleTapOnBackPress() {
        finish()
    }

    override fun onBackPressed() {
        if (fragmentBackStack!!.size < 1) {
            if (showBackMessage!!) {
                doubleTapOnBackPress()
            } else {
                finish()
                overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
        } else {
            if (!(fragmentBackStack!!.get(fragmentBackStack!!.size - 1) as BaseFragment).onFragmentBackPress()) {
                val currentFragment = fragmentBackStack!!.get(fragmentBackStack!!.size - 1)
                if (currentFragment is HomeFragment) {
                    doubleTapOnBackPress()
                } else if (currentFragment is ReportFragment) {
                    loadButtonUi(1)
                }  else {
                    popFragments()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun showProgressDialog(isCancelable: Boolean) {
        try {
            if (dialog != null && dialog?.isShowing!!) {
                dialog!!.dismiss()
            }
            dialog = ProgressDialog(this)
            //dialog!!.setMessage(getString(R.string.msg_please_wait))
            dialog!!.setCancelable(isCancelable)
            dialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun showProgress(ctx: Context) {
        if (customdialog != null) {
            try {
                if (customdialog!!.isShowing()) {
                    customdialog!!.dismiss()
                }
            } catch (e: Exception) {
                Log.d("Exception", "showProgress: " + e.toString())
            }
        }
        customdialog = Dialog(ctx, R.style.ActivityDialog);
        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog!!.getWindow()?.setGravity(Gravity.CENTER)
        customdialog!!.setCancelable(false)
       // customdialog!!.setContentView(R.layout.custom_progressbar)
        Log.d("TAG", "BeforeshowProgress: "+"Call")
        customdialog!!.show()
        Log.d("TAG", "AftershowProgress: "+"Call")
    }

    fun closeProgress() {
        try {
            if (customdialog != null && customdialog!!.isShowing()) {
                customdialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    @Synchronized
    fun pushAddFragments(fragment: Fragment?) {
        try {
            if (fragment != null) {
                fragmentBackStack?.push(fragment)
                val manager = supportFragmentManager
                val ft = manager.beginTransaction()
                ft.replace(
                    R.id.main_frame,
                    fragment,
                    fragmentBackStack?.size.toString()
                )
                ft.commit()
                manager.executePendingTransactions()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun popFragments() {
        var fragment: Fragment? = null
        if (fragmentBackStack!!.size > 1)
            fragment = fragmentBackStack!!.elementAt(fragmentBackStack!!.size - 2)
        else if (!fragmentBackStack!!.isEmpty())
            fragment = fragmentBackStack!!.elementAt(fragmentBackStack!!.size - 1)
        var manager: FragmentManager = supportFragmentManager
        var ft: FragmentTransaction = manager.beginTransaction()
        if (fragment != null) {
            manager.executePendingTransactions()
            if (fragment.isAdded())
                if (fragmentBackStack!!.size > 1) {
                    ft.show(fragment).commit()
                } else
                    ft.replace(R.id.main_frame, fragment).commit()
            fragmentBackStack!!.pop()
        }
        manager.executePendingTransactions()
    }

    fun loadButtonUi(position: Int) {
        when (position) {
            1 -> {
                if (selectedScreen != 1) {
                    selectedScreen = 1
                    imgBanner.visibility = View.VISIBLE
                    txt_Emergency.visibility = View.GONE
                    pushAddFragments(
                        HomeFragment.getInstance()
                    )
                }
            }
            2 -> {
                if (selectedScreen != 2) {
                    selectedScreen = 2
                    imgBanner.visibility = View.GONE
                    txt_Emergency.visibility = View.VISIBLE
                    txt_Emergency.setText("Coronavirus Self-Checker")
                    pushAddFragments(
                        TestFragment.getInstance()

                    )
                }
            }
            3 -> {
                if (selectedScreen != 3) {
                    selectedScreen = 3
                    imgBanner.visibility = View.VISIBLE
                    txt_Emergency.visibility = View.GONE
                    pushAddFragments(
                        HomeFragment.getInstance()
                    )
                }
            }
            4 -> {
                if (selectedScreen != 4) {
                    selectedScreen = 4
                    imgBanner.visibility = View.GONE
                    txt_Emergency.visibility = View.VISIBLE
                    txt_Emergency.setText("Medical Test")
                    pushAddFragments(
                        MedicalFragment.getInstance()
                    )
                }
            }
            5 -> {
                if (selectedScreen != 5) {
                    selectedScreen = 5
                    imgBanner.visibility = View.GONE
                    txt_Emergency.visibility = View.VISIBLE
                    txt_Emergency.setText("Emergency Test")
                    pushAddFragments(
                        KitFragment.getInstance()
                    )
                }
            }
            6->
            {
                if (selectedScreen != 6) {
                    selectedScreen = 6
                    imgBanner.visibility = View.GONE
                    txt_Emergency.visibility = View.VISIBLE
                    txt_Emergency.setText("History")
                    pushAddFragments(
                        HistoryFragment.getInstance()
                    )
                }
            }
            7->
            {
                if (selectedScreen != 7) {
                    selectedScreen = 7
                    imgBanner.visibility = View.GONE
                    txt_Emergency.visibility = View.VISIBLE
                    txt_Emergency.setText("Donate")
                    pushAddFragments(
                        DonateFragment.getInstance()
                    )
                }
            }
            8->
            {
                if (selectedScreen != 8) {
                    selectedScreen = 8
                    imgBanner.visibility = View.GONE
                    txt_Emergency.visibility = View.VISIBLE
                    txt_Emergency.setText("Update")
                    pushAddFragments(
                        UpdateFragment.getInstance()
                    )
                }
            }
        }
    }
}