package com.example.covid19.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.R
import com.example.covid19.model.CountryNameModel
import kotlinx.android.synthetic.main.item_country_name.view.*


class CountrySelectAdapter(
    var context: Context,
    var countryNameModels: ArrayList<CountryNameModel>,
    var onClick: OnClick
) : RecyclerView.Adapter<CountrySelectAdapter.ViewHolder>() {
    var country: String?=null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemModel : CountryNameModel)
        {
            itemView.img_Flag.setImageResource(itemModel.img_Flag!!)
            itemView.txt_countyName.setText(itemModel.txt_countyName)
            itemView.txt_minUpdate.setText(itemModel.txt_minUpdate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_country_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countryNameModels.get(position))
        country = countryNameModels.get(position).txt_countyName
        holder.itemView.setOnClickListener {
            onClick.onClick(position)
            /*fragment.country_Name.setText(countryNameModels.get(position).txt_countyName);
            slideDialog!!.dismiss();*/
        }
    }

    override fun getItemCount(): Int {
        return countryNameModels.size
    }

    interface OnClick
    {
        fun onClick(position: Int)
    }
}