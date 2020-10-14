package com.example.covid19.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid19.R
import com.example.covid19.ui.BaseActivity

class ViewPager2Fragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_pager2, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity =  activity as BaseActivity?
    }
    companion object {
        fun getInstance(): ViewPager2Fragment {
            return ViewPager2Fragment()
        }
        var mActivity: BaseActivity? = null
    }
}