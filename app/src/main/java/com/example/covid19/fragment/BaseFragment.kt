package com.example.covid19.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.covid19.ui.BaseActivity

open class BaseFragment : Fragment() {
    var baseActivityInstance: BaseActivity? = null

    fun onFragmentBackPress(): Boolean {
        return false
    }

    fun BaseFragment() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseFragment()
    }

    fun getBaseActivity(): BaseActivity? {
        return baseActivityInstance
    }

    fun setBaseActivity(baseActivity: BaseActivity) {
        this.baseActivityInstance = baseActivity
    }
}
