package com.example.covid19.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.R
import com.example.covid19.model.TimeModel
import kotlinx.android.synthetic.main.item_sanitizer.view.*

class SanitizerAdapter(var context: Context, var timeModel: ArrayList<TimeModel>) :
    RecyclerView.Adapter<SanitizerAdapter.ViewHolder>() {
    var myPos = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemModel: TimeModel) {
            itemView.txt_ml.setText(itemModel.txt_Time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sanitizer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeModel.get(position))
        if (myPos == position) {
            holder.itemView.sanitizer_cardView.setBackgroundResource(R.drawable.rectangle_red_cure_border);
        } else {
            holder.itemView.sanitizer_cardView.setBackgroundResource(R.drawable.rectangle_white_cure_border);
        }
        holder.itemView.setOnClickListener {
            myPos = position;
            notifyDataSetChanged();
        }
    }

    override fun getItemCount(): Int {
        return timeModel.size
    }
}