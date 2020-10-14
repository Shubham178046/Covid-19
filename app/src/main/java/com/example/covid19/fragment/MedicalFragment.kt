package com.example.covid19.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19.R
import com.example.covid19.adapter.NearCenterAdapter
import com.example.covid19.model.TimeModel
import com.example.covid19.ui.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_medical.*


class MedicalFragment : BaseFragment(), OnMapReadyCallback {
    var txt_Time = arrayOf("Select", "Select", "Select", "Select", "Select")
    var nearCenterAdapter: NearCenterAdapter? = null
    var timeModels: ArrayList<TimeModel>? = null
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as BaseActivity?
        val mapFragment = this.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this);
        initViews()
    }

    companion object {
        fun getInstance(): MedicalFragment {
            return MedicalFragment()
        }

        var mActivity: BaseActivity? = null
    }

    fun initViews() {
        recyclerView_nearCenter.layoutManager = LinearLayoutManager(mActivity)
        recyclerView_nearCenter.itemAnimator = DefaultItemAnimator()
        timeModels = ArrayList()

        for (i in 0 until txt_Time.size) {
            val timeModel = TimeModel()
            timeModel.txt_Time = txt_Time[i]
            timeModels!!.add(timeModel);
        }
        nearCenterAdapter = NearCenterAdapter(mActivity!!, timeModels!!)
        recyclerView_nearCenter.adapter = nearCenterAdapter
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val latLng = LatLng(30.648860, -81.465050)
        mMap = googleMap;
        googleMap!!.setMapStyle(
            MapStyleOptions(
                resources
                    .getString(R.string.style_json)
            )
        )
        val marker = MarkerOptions().position(latLng).title("Your are here")
        googleMap.addMarker(marker);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        val cameraPosition = CameraPosition.Builder().target(
            latLng
        ).zoom(30f).build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}