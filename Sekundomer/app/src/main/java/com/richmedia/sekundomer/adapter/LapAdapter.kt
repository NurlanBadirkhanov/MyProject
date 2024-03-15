package com.richmedia.sekundomer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.richmedia.sekundomer.R
import com.richmedia.sekundomer.model.Lap


class LapAdapter(
        private val context: Context,
        private var laps: MutableList<Lap>
) : BaseAdapter() {

    init {
        // sort latest index to top
        laps.sortByDescending {
            it.index
        }
    }

    override fun getCount(): Int {
        return laps.size
    }

    override fun getItem(position: Int): Any {
        return laps[position]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, _view: View?, viewGroup: ViewGroup): View? {
        val viewHolder: ViewHolder
        var view: View? = _view

        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.item_lap, viewGroup, false)

            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val lap = laps[i]
        viewHolder.tvIndex.text = lap.index.toString()
        viewHolder.tvDuration.text = Lap.convertToDuration(lap.lap)
        viewHolder.tvDiff.text = Lap.convertToDuration(lap.diff)

        return view
    }

    private class ViewHolder(itemView: View) {
        val tvIndex: TextView = itemView.findViewById(R.id.tv_index_lap_item)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration_lap_item)
        val tvDiff: TextView = itemView.findViewById(R.id.tv_diff_lap_item)
    }
}