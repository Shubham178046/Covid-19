package com.example.covid19.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid19.R
import com.example.covid19.ui.BaseActivity

class UpdateFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity =  activity as BaseActivity?
    }

    companion object {
        fun getInstance(): UpdateFragment {
            return UpdateFragment()
        }

        var mActivity: BaseActivity? = null
    }
}