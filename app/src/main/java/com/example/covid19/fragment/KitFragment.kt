package com.example.covid19.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.covid19.R
import com.example.covid19.adapter.SanitizerAdapter
import com.example.covid19.model.TimeModel
import com.example.covid19.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_kit.*


class KitFragment : BaseFragment() {
    var txt_Time = arrayOf("50 ml", "50 ml", "50 ml", "50 ml", "50 ml", "50 ml", "50 ml")
    var sanitizerAdapter: SanitizerAdapter? = null
    var timeModels: ArrayList<TimeModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity =  activity as BaseActivity?
        initViews()
    }

    fun initViews()
    {
        recyclerView_Sanitizer.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView_Sanitizer.setItemAnimator(DefaultItemAnimator())
        timeModels = ArrayList()
        for (i in 0 until txt_Time.size) {
            val timeModel = TimeModel()
            timeModel.txt_Time = txt_Time[i]
            timeModels!!.add(timeModel)
        }
        sanitizerAdapter = SanitizerAdapter(mActivity!!,timeModels!!)
        recyclerView_Sanitizer.adapter = sanitizerAdapter
    }
    companion object {
        fun getInstance(): KitFragment {
            return KitFragment()
        }

        var mActivity: BaseActivity? = null
    }
}