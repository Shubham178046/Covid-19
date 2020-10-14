package com.example.covid19.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.example.covid19.R
import com.example.covid19.adapter.TabViewPagerAdapter
import com.example.covid19.helper.Functions
import kotlinx.android.synthetic.main.activity_walkthrough.*


class WalkthroughActivity : BaseActivity() {
    var CURRENTPAGE = 2
    var tabViewPagerAdapter: TabViewPagerAdapter? = null

    companion object {
        fun launchActivity(activity: BaseActivity?) {
            if (activity != null) {
                Functions.fireIntent(activity, WalkthroughActivity::class.java, true, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(this.resources.getColor(R.color.status_color))
        }

        actionListner()

        li_line1.setBackgroundResource(R.drawable.lightorange_line)
        if (CURRENTPAGE < 2) {
            CURRENTPAGE++;
            viewPager.setCurrentItem(CURRENTPAGE);
            setcompletedStates(CURRENTPAGE);
            Log.e("CURRENTPAGE", CURRENTPAGE.toString() + " ");
        }
        tabViewPagerAdapter = TabViewPagerAdapter(getSupportFragmentManager());

        viewPager.adapter = tabViewPagerAdapter

        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                CURRENTPAGE = position;
                if (position == 2) {
                    btnNext.visibility = View.VISIBLE
                } else {
                    btnNext.visibility = View.GONE
                }
                setcompletedStates(CURRENTPAGE);
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    fun actionListner() {
        btnNext.setOnClickListener {
            SocialLoginActivity.launchActivity(this)
        }
    }

    private fun setcompletedStates(CURRENTPAGE: Int) {
        if (CURRENTPAGE == 0) {
            li_line1.setBackgroundResource(R.drawable.lightorange_line)
            li_line2.setBackgroundResource(R.drawable.lightgray_line)
            li_line3.setBackgroundResource(R.drawable.lightgray_line)
        }
        if (CURRENTPAGE == 1) {
            li_line1.setBackgroundResource(R.drawable.lightgray_line)
            li_line2.setBackgroundResource(R.drawable.lightorange_line)
            li_line3.setBackgroundResource(R.drawable.lightgray_line)
        }
        if (CURRENTPAGE == 2) {
            li_line1.setBackgroundResource(R.drawable.lightgray_line)
            li_line2.setBackgroundResource(R.drawable.lightgray_line)
            li_line3.setBackgroundResource(R.drawable.lightorange_line)
        }
    }

}