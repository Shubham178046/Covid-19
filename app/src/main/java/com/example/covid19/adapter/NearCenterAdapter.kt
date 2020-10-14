package com.example.covid19.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.R
import com.example.covid19.model.TimeModel
import com.example.covid19.ui.BaseActivity
import kotlinx.android.synthetic.main.item_nearcenter.view.*

class NearCenterAdapter(var context: BaseActivity, var timeModel: ArrayList<TimeModel>) :
    RecyclerView.Adapter<NearCenterAdapter.ViewHolder>() {
    var myPos = -1
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemModel: TimeModel)
        {
            itemView.txt_select.setText(itemModel.txt_Time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_nearcenter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeModel.get(position))
        if(myPos == position)
        {
            holder.itemView.select_cardView.setBackgroundResource(R.drawable.rectangle_cardselect_red);
            holder.itemView.txt_select.setTextColor(Color.parseColor("#ffffff"));
            holder.itemView.txt_select.setBackgroundResource(R.drawable.rectangle_red_cardselect);
        }
        else
        {
            holder.itemView.select_cardView.setBackgroundResource(R.drawable.rectangle_card_select);
            holder.itemView.txt_select.setTextColor(Color.parseColor("#9d9c9e"));
            holder.itemView.txt_select.setBackgroundResource(R.drawable.rectangle_gray_selectborder);
        }
        holder.itemView.select_cardView.setOnClickListener {
            myPos = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return timeModel.size
    }
}