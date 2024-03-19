package com.richmedia.secondsavvy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeAdapter(private val timeList: List<String>) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.time_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_time, parent, false)
        return TimeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val currentTime = timeList[position]
        holder.timeTextView.text = currentTime
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}
