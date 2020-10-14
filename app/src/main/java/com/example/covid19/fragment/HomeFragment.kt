package com.example.covid19.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.R
import com.example.covid19.adapter.CountrySelectAdapter
import com.example.covid19.model.CountryNameModel
import com.example.covid19.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    var countryNameModels: ArrayList<CountryNameModel>? = null
    var countryNameAdapter: CountrySelectAdapter? = null

    var img_Flag = arrayOf<Int>(
        R.drawable.ic_india, R.drawable.ic_canada, R.drawable.ic_mexico,
        R.drawable.ic_australia, R.drawable.ic_brazil, R.drawable.ic_russia, R.drawable.ic_mexico,
        R.drawable.ic_australia, R.drawable.ic_brazil, R.drawable.ic_russia
    )
    var txt_countyName = arrayOf(
        "India", "Canada", "Mexico", "Australia", "Brazil", "Russia", "Iran",
        "United Kingdom", "Switzerland", "Turkey"
    )
    var txt_minUpdate = arrayOf(
        "Updated 5 minutes ago",
        "Updated 12 minutes ago",
        "Updated an hour ago",
        "Updated 11 minutes ago",
        "Updated 5 minutes ago",
        "Updated 12 minutes ago",
        "Updated an hour ago",
        "Updated 11 minutes ago",
        "Updated an hour ago",
        "Updated 11 minutes ago"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity =  activity as BaseActivity?
        actionListner()
    }
    fun actionListner() {
        spinneFlag.setOnClickListener {
            slideDialog = Dialog(context!!, R.style.CustomDialogAnimation)
            slideDialog!!.setContentView(R.layout.item_layout_country)
            slideDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val window: Window? = slideDialog!!.window
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            val layoutParams = WindowManager.LayoutParams()
            slideDialog!!.window!!.attributes.windowAnimations = R.style.CustomDialogAnimation
            layoutParams.copyFrom(slideDialog!!.window!!.attributes)

            val width = (resources.displayMetrics.widthPixels * 0.60).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.65).toInt()

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = height
            layoutParams.gravity = Gravity.BOTTOM

            var recyclerView = slideDialog!!.findViewById<RecyclerView>(R.id.recyclerview_Country)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.itemAnimator = DefaultItemAnimator()
            countryNameModels = ArrayList()
            for (i in 0 until img_Flag.size) {
                var countryNameModel: CountryNameModel = CountryNameModel()
                countryNameModel.img_Flag = img_Flag[i]
                countryNameModel.txt_countyName = txt_countyName[i]
                countryNameModel.txt_minUpdate = txt_minUpdate[i]

                countryNameModels!!.add(countryNameModel)
            }
            countryNameAdapter =
                CountrySelectAdapter(context!!, countryNameModels!!, object : CountrySelectAdapter.OnClick{
                    override fun onClick(position: Int) {
                        country_Name.setText(countryNameModels!!.get(position).txt_countyName)
                        slideDialog!!.dismiss()
                    }
                })
            recyclerView.adapter = countryNameAdapter
            slideDialog!!.getWindow()!!.setAttributes(layoutParams);
            slideDialog!!.setCancelable(true);
            slideDialog!!.setCanceledOnTouchOutside(true);
            slideDialog!!.show();
        }
    }

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }

        var mActivity: BaseActivity? = null
        var slideDialog: Dialog? = null
    }
}