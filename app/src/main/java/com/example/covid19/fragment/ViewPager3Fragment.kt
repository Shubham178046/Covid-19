package com.example.covid19.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid19.R
import com.example.covid19.ui.BaseActivity

class ViewPager3Fragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager3, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       mActivity =  activity as BaseActivity?
    }
    companion object {
        fun getInstance(): ViewPager3Fragment {
            return ViewPager3Fragment()
        }
        var mActivity: BaseActivity? = null
    }
}